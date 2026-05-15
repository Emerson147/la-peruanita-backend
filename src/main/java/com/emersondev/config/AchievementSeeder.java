package com.emersondev.config;

import com.emersondev.domain.model.Achievement;
import com.emersondev.domain.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementSeeder implements CommandLineRunner {

  private final AchievementRepository achievementRepository;

  @Override
  public void run(String... args) {
    if (achievementRepository.findAll().isEmpty()) {
      log.info("Inicializando logros predefinidos...");
      seedAchievements();
    }
  }

  private void seedAchievements() {
    List<Achievement> logros = List.of(
            build("ach-first-sale", "Primera Venta",
                    "Completa tu primera venta",
                    "sell", "sales", "bronze", 1, 50),
            build("ach-10-sales", "Vendedor Novato",
                    "Completa 10 ventas",
                    "shopping_bag", "sales", "bronze", 10, 100),
            build("ach-50-sales", "Vendedor Experimentado",
                    "Completa 50 ventas",
                    "local_mall", "sales", "silver", 50, 300),
            build("ach-100-sales", "Vendedor Profesional",
                    "Completa 100 ventas",
                    "workspace_premium", "sales", "gold", 100, 500),
            build("ach-500-sales", "Maestro de Ventas",
                    "Completa 500 ventas",
                    "stars", "sales", "platinum", 500, 1000),
            build("ach-500-revenue", "Primeros Ingresos",
                    "Genera S/ 500 en ventas",
                    "payments", "revenue", "bronze", 500, 100),
            build("ach-2k-revenue", "Vendedor Constante",
                    "Genera S/ 2,000 en ventas",
                    "account_balance_wallet", "revenue", "silver", 2000, 200),
            build("ach-4k-revenue", "Meta Mensual",
                    "Genera S/ 4,000 en ventas",
                    "trending_up", "revenue", "gold", 4000, 300),
            build("ach-5k-revenue", "Mes Excepcional",
                    "Genera S/ 5,000 en ventas",
                    "monetization_on", "revenue", "platinum", 5000, 500),
            build("ach-streak-7", "Racha de Fuego",
                    "7 días consecutivos con ventas",
                    "local_fire_department", "streak", "silver", 7, 200),
            build("ach-streak-30", "Imparable",
                    "30 días consecutivos con ventas",
                    "whatshot", "streak", "gold", 30, 500),
            build("ach-big-sale", "Venta Grande",
                    "Venta de más de S/ 200",
                    "workspace_premium", "special", "silver", 1, 150),
            build("ach-early-bird", "Madrugador",
                    "Primera venta del día antes de las 10am",
                    "wb_sunny", "special", "bronze", 1, 50)
    );

    logros.forEach(achievementRepository::save);
    log.info("{} logros inicializados.", logros.size());
  }

  private Achievement build(
          String key, String title, String description,
          String icon, String category, String tier,
          int requirement, int points) {
    Achievement a = new Achievement();
    a.setAchievementKey(key);
    a.setTitle(title);
    a.setDescription(description);
    a.setIcon(icon);
    a.setCategory(category);
    a.setTier(tier);
    a.setRequirement(requirement);
    a.setPoints(points);
    return a;
  }
}