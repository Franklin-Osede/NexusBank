package com.nexusbank.infrastructure.persistence.adapter;

import com.nexusbank.domain.model.User;
import com.nexusbank.infrastructure.persistence.entity.UserEntity;
import com.nexusbank.infrastructure.persistence.mapper.UserMapper;
import com.nexusbank.infrastructure.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserPersistenceAdapterTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  private UserPersistenceAdapter userPersistenceAdapter;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    userPersistenceAdapter = new UserPersistenceAdapter(userRepository, userMapper);
  }

  @Test
  void loadUser_shouldReturnUserWhenFound() {
    // Given
    String userId = "user-123";
    UserEntity userEntity = new UserEntity();
    userEntity.setId(userId);
    userEntity.setEmail("test@example.com");
    userEntity.setName("Test User");

    // Crear un usuario de dominio usando el método createNew
    User domainUser = User.createNew(userId, "Test User", "test@example.com", "hashedPassword");

    when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
    when(userMapper.toDomainEntity(userEntity)).thenReturn(domainUser);

    // When
    Optional<User> result = userPersistenceAdapter.loadUser(userId);

    // Then
    assertTrue(result.isPresent());
    assertEquals(userId, result.get().getId());
    assertEquals("test@example.com", result.get().getEmail());
    verify(userRepository).findById(userId);
    verify(userMapper).toDomainEntity(userEntity);
  }

  @Test
  void loadUser_shouldReturnEmptyWhenNotFound() {
    // Given
    String userId = "non-existent-id";
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    // When
    Optional<User> result = userPersistenceAdapter.loadUser(userId);

    // Then
    assertFalse(result.isPresent());
    verify(userRepository).findById(userId);
    verifyNoInteractions(userMapper);
  }

  @Test
  void findUserByEmail_shouldReturnUserWhenFound() {
    // Given
    String email = "test@example.com";
    UserEntity userEntity = new UserEntity();
    userEntity.setId("user-123");
    userEntity.setEmail(email);
    userEntity.setName("Test User");

    // Crear un usuario de dominio usando el método createNew
    User domainUser = User.createNew("user-123", "Test User", email, "hashedPassword");

    when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
    when(userMapper.toDomainEntity(userEntity)).thenReturn(domainUser);

    // When
    Optional<User> result = userPersistenceAdapter.findUserByEmail(email);

    // Then
    assertTrue(result.isPresent());
    assertEquals("user-123", result.get().getId());
    assertEquals(email, result.get().getEmail());
    verify(userRepository).findByEmail(email);
    verify(userMapper).toDomainEntity(userEntity);
  }

  @Test
  void saveUser_shouldSaveUser() {
    // Given
    String userId = "user-123";
    String email = "test@example.com";
    String name = "Test User";

    // Crear un usuario de dominio usando el método createNew
    User user = User.createNew(userId, name, email, "hashedPassword");

    UserEntity userEntity = new UserEntity();
    userEntity.setId(userId);
    userEntity.setEmail(email);
    userEntity.setName(name);

    when(userMapper.toJpaEntity(user)).thenReturn(userEntity);
    when(userRepository.save(userEntity)).thenReturn(userEntity);

    // When
    userPersistenceAdapter.saveUser(user);

    // Then
    verify(userMapper).toJpaEntity(user);
    verify(userRepository).save(userEntity);
  }
}