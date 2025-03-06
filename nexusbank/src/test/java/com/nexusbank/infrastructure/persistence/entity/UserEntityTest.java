package com.nexusbank.infrastructure.persistence.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {

  @Test
  void shouldCreateUserEntity() {
    // Given
    String id = "user-123";
    String email = "test@example.com";
    String name = "Test User";
    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime updatedAt = createdAt;

    // When
    UserEntity userEntity = new UserEntity();
    userEntity.setId(id);
    userEntity.setEmail(email);
    userEntity.setName(name);
    userEntity.setCreatedAt(createdAt);
    userEntity.setUpdatedAt(updatedAt);

    // Then
    assertEquals(id, userEntity.getId());
    assertEquals(email, userEntity.getEmail());
    assertEquals(name, userEntity.getName());
    assertEquals(createdAt, userEntity.getCreatedAt());
    assertEquals(updatedAt, userEntity.getUpdatedAt());
  }

  @Test
  void shouldImplementEqualsAndHashCodeBasedOnId() {
    // Given
    String commonId = "user-123";

    UserEntity entity1 = new UserEntity();
    entity1.setId(commonId);
    entity1.setEmail("test1@example.com");

    UserEntity entity2 = new UserEntity();
    entity2.setId(commonId);
    entity2.setEmail("test2@example.com"); // Different email but same ID

    UserEntity entity3 = new UserEntity();
    entity3.setId("user-456"); // Different ID
    entity3.setEmail("test1@example.com");

    // Then
    assertEquals(entity1, entity2);
    assertEquals(entity1.hashCode(), entity2.hashCode());
    assertNotEquals(entity1, entity3);
    assertNotEquals(entity1.hashCode(), entity3.hashCode());
  }
}