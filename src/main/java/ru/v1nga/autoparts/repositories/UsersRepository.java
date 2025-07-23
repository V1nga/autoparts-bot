package ru.v1nga.autoparts.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.v1nga.autoparts.entities.UserEntity;

import java.util.Optional;

public interface UsersRepository extends CrudRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}
