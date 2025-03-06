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
      container = new DatabaseTestContainer();
      container.start();
    }
    return container;
  }

  @Override
  public void stop() {
    // Se omite detener el contenedor para reutilizarlo durante la JVM
  }
}
