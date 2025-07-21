package ru.v1nga.autoparts.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.v1nga.autoparts.entities.CartItemEntity;
import ru.v1nga.autoparts.entities.PartEntity;

import java.util.List;
import java.util.Optional;

public interface CartItemsRepository extends CrudRepository<CartItemEntity, Long> {
    List<CartItemEntity> findByUserId(Long userId);
    Optional<CartItemEntity> findByUserIdAndPart(Long userId, PartEntity partEntity);
}
