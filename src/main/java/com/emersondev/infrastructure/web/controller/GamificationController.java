package com.emersondev.infrastructure.web.controller;

import com.emersondev.domain.model.*;
import com.emersondev.domain.repository.*;
import com.emersondev.domain.service.GamificationDomainService;
import com.emersondev.infrastructure.persistence.repository.UsuarioRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/gamification")
@RequiredArgsConstructor
public class GamificationController {

        public static class GoalRequestDTO {
                public UUID userId;
                public String title;
                public String description;
                public String metric;
                public Integer target;
        }

        private final GamificationDomainService gamificationService;
        private final AchievementRepository achievementRepository;
        private final UserStatsRepository userStatsRepository;
        private final GoalRepository goalRepository;
        private final UsuarioRepositoryAdapter usuarioRepository;

        // GET /api/gamification/achievements
        // Logros globales (los 13 predefinidos)
        @GetMapping("/achievements")
        public ResponseEntity<List<Achievement>> obtenerLogros() {
                return ResponseEntity.ok(
                                achievementRepository.findAll());
        }

        // GET /api/gamification/me/achievements
        // Logros del usuario autenticado con su progreso
        @GetMapping("/me/achievements")
        public ResponseEntity<List<UserAchievement>> misLogros(
                        @AuthenticationPrincipal UserDetails userDetails) {

                UUID userId = obtenerUserId(userDetails.getUsername());
                List<UserAchievement> logros = achievementRepository.findByUserId(userId);

                // Si no tiene logros inicializados, inicializarlos
                if (logros.isEmpty()) {
                        gamificationService.inicializarLogrosUsuario(userId);
                        logros = achievementRepository.findByUserId(userId);
                }

                return ResponseEntity.ok(logros);
        }

        // GET /api/gamification/me/stats
        // Estadísticas del usuario autenticado
        @GetMapping("/me/stats")
        public ResponseEntity<UserStats> misStats(
                        @AuthenticationPrincipal UserDetails userDetails) {

                UUID userId = obtenerUserId(userDetails.getUsername());

                UserStats stats = userStatsRepository
                                .findByUserId(userId)
                                .orElseGet(() -> {
                                        gamificationService
                                                        .inicializarLogrosUsuario(userId);
                                        return userStatsRepository
                                                        .findByUserId(userId).orElseThrow();
                                });

                return ResponseEntity.ok(stats);
        }

        // GET /api/gamification/me/goals
        // Metas activas del usuario autenticado
        @GetMapping("/me/goals")
        public ResponseEntity<List<Goal>> misMetas(
                        @AuthenticationPrincipal UserDetails userDetails) {

                UUID userId = obtenerUserId(userDetails.getUsername());
                List<Goal> metas = goalRepository
                                .findByUserIdAndStatus(userId, "active");

                if (metas.isEmpty()) {
                        gamificationService.inicializarMetasDefecto(userId);
                        metas = goalRepository.findByUserIdAndStatus(userId, "active");
                }

                return ResponseEntity.ok(metas);
        }

        // Ranking de todos los usuarios
        // GET /api/gamification/leaderboard
        @GetMapping("/leaderboard")
        public ResponseEntity<List<Map<String, Object>>> leaderboard() {

                List<com.emersondev.domain.model.Usuario> allUsers = usuarioRepository.findAll();

                List<Map<String, Object>> ranking = allUsers.stream()
                                .map(user -> {
                                        UserStats stats = userStatsRepository.findByUserId(user.getId()).orElse(null);

                                        int totalPoints = stats != null && stats.getTotalPoints() != null ? stats.getTotalPoints() : 0;
                                        int level = stats != null && stats.getLevel() != null ? stats.getLevel() : 1;
                                        int totalSales = stats != null && stats.getTotalSalesCompleted() != null ? stats.getTotalSalesCompleted() : 0;
                                        java.math.BigDecimal totalRevenue = stats != null && stats.getTotalRevenueGenerated() != null ? stats.getTotalRevenueGenerated() : java.math.BigDecimal.ZERO;
                                        int currentStreak = stats != null && stats.getCurrentStreak() != null ? stats.getCurrentStreak() : 0;
                                        
                                        int logrosDesbloqueados = achievementRepository
                                                        .findByUserId(user.getId())
                                                        .stream()
                                                        .filter(UserAchievement::isUnlocked)
                                                        .mapToInt(ua -> 1)
                                                        .sum();

                                        Map<String, Object> entry = new LinkedHashMap<>();
                                        entry.put("userId", user.getId());
                                        entry.put("userName", user.getNombre() != null ? user.getNombre() : "Usuario");
                                        entry.put("totalPoints", totalPoints);
                                        entry.put("level", level);
                                        entry.put("totalSales", totalSales);
                                        entry.put("totalRevenue", totalRevenue);
                                        entry.put("currentStreak", currentStreak);
                                        entry.put("achievementsCount", logrosDesbloqueados);
                                        
                                        return entry;
                                })
                                .sorted((a, b) -> Integer.compare(
                                                (Integer) b.get("totalPoints"),
                                                (Integer) a.get("totalPoints")))
                                .toList();

                return ResponseEntity.ok(ranking);
        }

        // POST /api/gamification/me/initialize
        // Inicializar logros para el usuario actual
        @PostMapping("/me/initialize")
        public ResponseEntity<Void> inicializar(
                        @AuthenticationPrincipal UserDetails userDetails) {

                UUID userId = obtenerUserId(userDetails.getUsername());
                gamificationService.inicializarLogrosUsuario(userId);
                return ResponseEntity.ok().build();
        }

        // Helper — obtener UUID del usuario por email
        private UUID obtenerUserId(String email) {
                return usuarioRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException(
                                                "Usuario no encontrado: " + email))
                                .getId();
        }

        // POST /api/gamification/admin/goals
        @PostMapping("/admin/goals")
        public ResponseEntity<Goal> asignarMetaComoAdmin(
                        @RequestBody GoalRequestDTO request) {

                Goal nMeta = new Goal();
                nMeta.setUserId(request.userId);
                nMeta.setTitle(request.title != null ? request.title : "Meta Asignada");
                nMeta.setDescription(request.description != null ? request.description : "Meta propuesta por administrador");
                nMeta.setType("monthly");
                nMeta.setMetric(request.metric != null ? request.metric : "sales_count");
                nMeta.setTarget(request.target != null ? request.target : 1);
                nMeta.setCurrent(0);
                nMeta.setStartDate(LocalDateTime.now());
                nMeta.setEndDate(LocalDateTime.now().plusMonths(1));
                nMeta.setStatus("active");

                Goal saved = goalRepository.save(nMeta);
                return ResponseEntity.ok(saved);
        }
}