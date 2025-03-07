package com.nexusbank.application.infrastructure.persistence.integration;

import com.nexusbank.application.infrastructure.persistence.container.TestDatabaseConfig;
import com.nexusbank.infrastructure.persistence.entity.UserEntity;
import com.nexusbank.infrastructure.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@ContextConfiguration(initializers = TestDatabaseConfig.class)
public class UserRepositoryIntegrationTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  public void testSaveAndFindUser() {
    // Crear un nuevo UserEntity
    UserEntity user = new UserEntity();
    user.setId("user-123");
    user.setName("John Doe");
    user.setEmail("john.doe@example.com");
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdatedAt(LocalDateTime.now());

    // Guardar el usuario
    userRepository.save(user);

    // Recuperar el usuario por ID
    Optional<UserEntity> retrieved = userRepository.findById("user-123");
    assertTrue(retrieved.isPresent());
    assertEquals("John Doe", retrieved.get().getName());
    assertEquals("john.doe@example.com", retrieved.get().getEmail());
  }
}