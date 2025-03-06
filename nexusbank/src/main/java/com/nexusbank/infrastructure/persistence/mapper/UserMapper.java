package com.nexusbank.infrastructure.persistence.mapper;

import com.nexusbank.domain.model.User;
import com.nexusbank.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {

  // Este método necesitará ser modificado con datos simulados para passwordHash
  // debido a que UserEntity no tiene todos los campos necesarios
  public User toDomainEntity(UserEntity entity) {
    // Valor simulado para passwordHash ya que no está en la entidad
    String defaultPasswordHash = "NO_PASSWORD_STORED";

    // Utilizamos el factory method para crear un nuevo usuario
    User user = User.createNew(
        entity.getId(),
        entity.getName(),
        entity.getEmail(),
        defaultPasswordHash);

    return user;
  }

  public UserEntity toJpaEntity(User user) {
    UserEntity entity = new UserEntity();
    entity.setId(user.getId());
    entity.setEmail(user.getEmail());
    entity.setName(user.getName());

    // Usamos las fechas del dominio si están disponibles, de lo contrario usamos la
    // fecha actual
    LocalDateTime now = LocalDateTime.now();
    entity.setCreatedAt(user.getCreatedAt() != null ? user.getCreatedAt() : now);
    entity.setUpdatedAt(user.getUpdatedAt() != null ? user.getUpdatedAt() : now);

    return entity;
  }
}