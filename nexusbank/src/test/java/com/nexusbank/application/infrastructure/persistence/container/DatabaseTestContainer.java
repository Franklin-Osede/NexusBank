package com.nexusbank.application.infrastructure.persistence.container;

import org.testcontainers.containers.PostgreSQLContainer;

public class DatabaseTestContainer extends PostgreSQLContainer<DatabaseTestContainer> {
  private static final String IMAGE_VERSION = "postgres:14-alpine";
  private static DatabaseTestContainer container;

  private DatabaseTestContainer() {
    super(IMAGE_VERSION);
  }

  public static DatabaseTestContainer getInstance() {
    if (container == null) {
      System.out.println("Creando nueva instancia de DatabaseTestContainer");
      container = new DatabaseTestContainer();
      container.withDatabaseName("test_db")
          .withUsername("test")
          .withPassword("test");

      try {
        System.out.println("Iniciando contenedor PostgreSQL...");
        container.start();
        System.out.println("Contenedor PostgreSQL iniciado correctamente");
        System.out.println("JDBC URL: " + container.getJdbcUrl());
        System.out.println("Usuario: " + container.getUsername());
        System.out.println("Contraseña: " + container.getPassword());
        System.out.println("Puerto mapeado: " + container.getFirstMappedPort());
      } catch (Exception e) {
        System.err.println("ERROR al iniciar el contenedor: " + e.getMessage());
        e.printStackTrace();
      }
    } else {
      System.out.println("Usando instancia existente de DatabaseTestContainer");
      System.out.println("JDBC URL existente: " + container.getJdbcUrl());
    }
    return container;
  }

  @Override
  public void stop() {
    // Se omite detener el contenedor para reutilizarlo durante la JVM
    System.out.println("Método stop() llamado pero ignorado para reutilizar el contenedor");
  }
}