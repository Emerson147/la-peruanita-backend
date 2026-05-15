package com.emersondev.domain.service;

import com.emersondev.domain.model.*;
import com.emersondev.domain.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GamificationDomainService {

        private final AchievementRepository achievementRepository;
        private final UserStatsRepository userStatsRepository;
        private final GoalRepository goalRepository;
        private final VentaRepository ventaRepository;

        // ============================================
        // VERIFICAR LOGROS AL REGISTRAR VENTA
        // ============================================

        @Transactional
        public void verificarLogros(UUID userId, Venta ventaRegistrada) {
                log.info("Verificando logros para usuario: {}", userId);

                // Obtener todas las ventas del usuario
                List<Venta> ventasUsuario = new ArrayList<>(ventaRepository
                                .findByStatus("completed").stream()
                                .filter(v -> userId.equals(v.getVendedorId())
                                                || userId.equals(v.getUserId()))
                                .toList());

                // Asegurar que la venta actual esté incluida (prevenir problemas de caché JPA flush)
                boolean contieneVentaActual = ventasUsuario.stream()
                                .anyMatch(v -> v.getId() != null && v.getId().equals(ventaRegistrada.getId()));
                
                if (!contieneVentaActual && "completed".equals(ventaRegistrada.getStatus())) {
                    ventasUsuario.add(ventaRegistrada);
                }

                int totalVentas = ventasUsuario.size();
                BigDecimal totalIngresos = ventasUsuario.stream()
                                .map(Venta::getTotal)
                                .filter(Objects::nonNull)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                int rachaActual = calcularRacha(ventasUsuario);

                // Obtener logros del usuario
                List<UserAchievement> logros = achievementRepository
                                .findByUserId(userId);

                // Verificar cada logro
                for (UserAchievement logro : logros) {
                        if (logro.isUnlocked())
                                continue;

                        int nuevoProgreso = calcularProgreso(
                                        logro.getAchievementKey(),
                                        totalVentas,
                                        totalIngresos,
                                        rachaActual,
                                        ventaRegistrada);

                        logro.setProgress(nuevoProgreso);

                        if (logro.puedeDesbloquear()) {
                                logro.desbloquear();
                                log.info("¡Logro desbloqueado! {} para usuario {}",
                                                logro.getTitle(), userId);

                                // Sumar puntos al usuario
                                agregarPuntos(userId, logro.getPoints());
                        }

                        achievementRepository.saveUserAchievement(logro);
                }

                // Actualizar stats del usuario
                actualizarStats(userId, totalVentas,
                                totalIngresos, rachaActual);

                // Actualizar metas pasándole la lista entera para poder filtrar
                actualizarMetas(userId, ventasUsuario);
        }

        // ============================================
        // CÁLCULO DE PROGRESO POR TIPO DE LOGRO
        // ============================================

        private int calcularProgreso(
                        String key,
                        int totalVentas,
                        BigDecimal totalIngresos,
                        int racha,
                        Venta ventaActual) {

                return switch (key) {
                        // Sales count
                        case "ach-first-sale",
                                        "ach-10-sales",
                                        "ach-50-sales",
                                        "ach-100-sales",
                                        "ach-500-sales" ->
                                totalVentas;

                        // Revenue
                        case "ach-500-revenue",
                                        "ach-2k-revenue",
                                        "ach-4k-revenue",
                                        "ach-5k-revenue" ->
                                totalIngresos.intValue();

                        // Streak
                        case "ach-streak-7",
                                        "ach-streak-30" ->
                                racha;

                        // Big sale — venta > S/200
                        case "ach-big-sale" ->
                                ventaActual.getTotal() != null &&
                                                ventaActual.getTotal().compareTo(
                                                                new BigDecimal("200")) >= 0 ? 1 : 0;

                        // Early bird — venta antes de las 10am
                        case "ach-early-bird" ->
                                ventaActual.getCreatedAt() != null &&
                                                ventaActual.getCreatedAt().getHour() < 10 ? 1 : 0;

                        default -> 0;
                };
        }

        // ============================================
        // CÁLCULO DE RACHA
        // ============================================

        private int calcularRacha(List<Venta> ventas) {
                if (ventas.isEmpty())
                        return 0;

                LocalDateTime hoy = LocalDateTime.now()
                                .truncatedTo(ChronoUnit.DAYS);

                Set<LocalDateTime> diasConVentas = new HashSet<>();
                ventas.forEach(v -> {
                        if (v.getCreatedAt() != null) {
                                diasConVentas.add(v.getCreatedAt()
                                                .truncatedTo(ChronoUnit.DAYS));
                        }
                });

                int racha = 0;
                LocalDateTime diaActual = hoy;

                while (diasConVentas.contains(diaActual)) {
                        racha++;
                        diaActual = diaActual.minusDays(1);
                }

                return racha;
        }

        // ============================================
        // ACTUALIZAR STATS DEL USUARIO
        // ============================================

        private void actualizarStats(
                        UUID userId,
                        int totalVentas,
                        BigDecimal totalIngresos,
                        int rachaActual) {

                UserStats stats = userStatsRepository
                                .findByUserId(userId)
                                .orElseGet(() -> {
                                        UserStats nuevo = new UserStats();
                                        nuevo.setUserId(userId);
                                        nuevo.setTotalPoints(0);
                                        nuevo.setLevel(1);
                                        nuevo.setCurrentStreak(0);
                                        nuevo.setLongestStreak(0);
                                        nuevo.setJoinedAt(LocalDateTime.now());
                                        return nuevo;
                                });

                stats.setTotalSalesCompleted(totalVentas);
                stats.setTotalRevenueGenerated(totalIngresos);
                stats.setCurrentStreak(rachaActual);

                if (rachaActual > (stats.getLongestStreak() != null
                                ? stats.getLongestStreak()
                                : 0)) {
                        stats.setLongestStreak(rachaActual);
                }

                userStatsRepository.save(stats);
        }

        private void agregarPuntos(UUID userId, int puntos) {
                UserStats stats = userStatsRepository
                                .findByUserId(userId)
                                .orElseGet(() -> {
                                        UserStats nuevo = new UserStats();
                                        nuevo.setUserId(userId);
                                        nuevo.setTotalPoints(0);
                                        nuevo.setLevel(1);
                                        nuevo.setJoinedAt(LocalDateTime.now());
                                        return nuevo;
                                });

                stats.agregarPuntos(puntos);
                userStatsRepository.save(stats);
                log.info("+{} puntos para usuario {}. Total: {}",
                                puntos, userId, stats.getTotalPoints());
        }

        // ============================================
        // ACTUALIZAR METAS
        // ============================================

        private void actualizarMetas(
                        UUID userId,
                        List<Venta> ventasUsuario) {

                log.info("📊 Actualizando metas para usuario {} con {} ventas en histórico", 
                        userId, ventasUsuario.size());

                List<Goal> metas = goalRepository
                                .findByUserIdAndStatus(userId, "active");

                LocalDateTime ahora = LocalDateTime.now();

                for (Goal meta : metas) {
                        // Verificar que la meta no ha expirado
                        if (meta.getEndDate() != null &&
                                        ahora.isAfter(meta.getEndDate())) {
                                meta.setStatus("failed");
                                goalRepository.save(meta);
                                continue;
                        }

                        // Filtrar las ventas que ESTÉN dentro del periodo de la meta
                        List<Venta> ventasEnPeriodo = ventasUsuario.stream()
                                        .filter(v -> v.getCreatedAt() != null && 
                                                     !v.getCreatedAt().isBefore(meta.getStartDate()) && 
                                                     (meta.getEndDate() == null || !v.getCreatedAt().isAfter(meta.getEndDate())))
                                        .toList();

                        int totalVentasPeriodo = ventasEnPeriodo.size();
                        BigDecimal totalIngresosPeriodo = ventasEnPeriodo.stream()
                                        .map(Venta::getTotal)
                                        .filter(Objects::nonNull)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                        // Calcular progreso según métrica EN EL PERIODO
                        int nuevoProgreso = switch (meta.getMetric()) {
                                case "sales_count" -> totalVentasPeriodo;
                                case "revenue" -> totalIngresosPeriodo.intValue();
                                default -> 0;
                        };

                        meta.setCurrent(nuevoProgreso);

                        if (meta.estaCompleta()) {
                                meta.setStatus("completed");
                                log.info("¡Meta completada! {} para usuario {}",
                                                meta.getTitle(), userId);
                        }

                        goalRepository.save(meta);
                }
        }

        // ============================================
        // INICIALIZAR LOGROS PARA NUEVO USUARIO
        // ============================================

        @Transactional
        public void inicializarLogrosUsuario(UUID userId) {
                log.info("Inicializando logros para usuario: {}", userId);

                List<Achievement> todosLosLogros = achievementRepository.findAll();

                for (Achievement logro : todosLosLogros) {
                        // Verificar que no exista ya
                        boolean yaExiste = achievementRepository
                                        .findByUserIdAndKey(userId,
                                                        logro.getAchievementKey())
                                        .isPresent();

                        if (!yaExiste) {
                                UserAchievement ua = new UserAchievement();
                                ua.setUserId(userId);
                                ua.setAchievementId(logro.getId());
                                ua.setAchievementKey(logro.getAchievementKey());
                                ua.setTitle(logro.getTitle());
                                ua.setDescription(logro.getDescription());
                                ua.setIcon(logro.getIcon());
                                ua.setCategory(logro.getCategory());
                                ua.setTier(logro.getTier());
                                ua.setRequirement(logro.getRequirement());
                                ua.setPoints(logro.getPoints());
                                ua.setProgress(0);
                                ua.setUnlocked(false);

                                achievementRepository.saveUserAchievement(ua);
                        }
                }

                // Inicializar stats
                userStatsRepository.findByUserId(userId).orElseGet(() -> {
                        UserStats stats = new UserStats();
                        stats.setUserId(userId);
                        stats.setTotalPoints(0);
                        stats.setLevel(1);
                        stats.setCurrentStreak(0);
                        stats.setLongestStreak(0);
                        stats.setTotalSalesCompleted(0);
                        stats.setTotalRevenueGenerated(BigDecimal.ZERO);
                        stats.setJoinedAt(LocalDateTime.now());
                        return userStatsRepository.save(stats);
                });

                // Inicializar metas por defecto
                inicializarMetasDefecto(userId);
        }

        public void inicializarMetasDefecto(UUID userId) {
                List<Goal> metasExistentes = goalRepository
                                .findByUserIdAndStatus(userId, "active");

                if (!metasExistentes.isEmpty())
                        return;

                LocalDateTime ahora = LocalDateTime.now();

                // Meta semanal
                Goal metaSemanal = new Goal();
                metaSemanal.setUserId(userId);
                metaSemanal.setTitle("Meta Semanal");
                metaSemanal.setDescription("10 ventas esta semana");
                metaSemanal.setType("weekly");
                metaSemanal.setMetric("sales_count");
                metaSemanal.setTarget(10);
                metaSemanal.setCurrent(0);
                metaSemanal.setStartDate(ahora);
                metaSemanal.setEndDate(ahora.plusDays(7));
                metaSemanal.setStatus("active");
                goalRepository.save(metaSemanal);

                // Meta mensual
                Goal metaMensual = new Goal();
                metaMensual.setUserId(userId);
                metaMensual.setTitle("Meta Mensual");
                metaMensual.setDescription("S/ 4,000 en ventas este mes");
                metaMensual.setType("monthly");
                metaMensual.setMetric("revenue");
                metaMensual.setTarget(4000);
                metaMensual.setCurrent(0);
                metaMensual.setStartDate(ahora.withDayOfMonth(1)
                                .withHour(0).withMinute(0));
                metaMensual.setEndDate(ahora.plusMonths(1)
                                .withDayOfMonth(1).minusDays(1));
                metaMensual.setStatus("active");
                goalRepository.save(metaMensual);
        }
}