package ru.v1nga.autoparts.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.v1nga.autoparts.entities.OrderEntity;

import java.util.List;

public interface OrdersRepository extends CrudRepository<OrderEntity, Long> {
    List<OrderEntity> findByUserId(Long userId);
}
