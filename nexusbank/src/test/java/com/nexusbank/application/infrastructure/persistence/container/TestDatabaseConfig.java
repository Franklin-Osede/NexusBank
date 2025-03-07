package com.nexusbank.application.infrastructure.persistence.container;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class TestDatabaseConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    System.out.println("Inicializando TestDatabaseConfig...");

    try {
      // Se obtiene la instancia del contenedor de base de datos
      DatabaseTestContainer container = DatabaseTestContainer.getInstance();

      // Se inyectan las propiedades del contenedor en el entorno de Spring
      System.out.println("Configurando propiedades de Spring con:");
      System.out.println("URL: " + container.getJdbcUrl());
      System.out.println("Usuario: " + container.getUsername());
      System.out.println("Contraseña: " + container.getPassword());

      TestPropertyValues.of(
          "spring.datasource.url=" + container.getJdbcUrl(),
          "spring.datasource.username=" + container.getUsername(),
          "spring.datasource.password=" + container.getPassword(),
          "spring.datasource.driver-class-name=org.postgresql.Driver").applyTo(applicationContext.getEnvironment());

      System.out.println("Propiedades configuradas correctamente");
    } catch (Exception e) {
      System.err.println("ERROR en TestDatabaseConfig.initialize: " + e.getMessage());
      e.printStackTrace();
    }
  }
}