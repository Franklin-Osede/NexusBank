package com.nexusbank.application.infrastructure.persistence.container;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class TestDatabaseConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    // Se obtiene la instancia del contenedor de base de datos
    DatabaseTestContainer container = DatabaseTestContainer.getInstance();
    // Se inyectan las propiedades del contenedor en el entorno de Spring
    TestPropertyValues.of(
        "spring.datasource.url=" + container.getJdbcUrl(),
        "spring.datasource.username=" + container.getUsername(),
        "spring.datasource.password=" + container.getPassword()).applyTo(applicationContext.getEnvironment());
  }
}
