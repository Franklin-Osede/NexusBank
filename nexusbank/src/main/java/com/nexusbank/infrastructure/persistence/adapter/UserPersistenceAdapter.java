package com.nexusbank.infrastructure.persistence.adapter;

import com.nexusbank.application.port.out.LoadUserPort;
import com.nexusbank.application.port.out.SaveUserPort;
import com.nexusbank.domain.model.User;
import com.nexusbank.infrastructure.persistence.entity.UserEntity;
import com.nexusbank.infrastructure.persistence.mapper.UserMapper;
import com.nexusbank.infrastructure.persistence.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserPersistenceAdapter implements LoadUserPort, SaveUserPort {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserPersistenceAdapter(UserRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  @Override
  public Optional<User> loadUser(String userId) {
    return userRepository.findById(userId)
        .map(userMapper::toDomainEntity);
  }

  @Override
  public Optional<User> findUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .map(userMapper::toDomainEntity);
  }

  @Override
  public void saveUser(User user) {
    UserEntity userEntity = userMapper.toJpaEntity(user);
    userRepository.save(userEntity);
  }
}