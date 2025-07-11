package ru.v1nga.autoparts.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.v1nga.autoparts.entities.PartEntity;

public interface PartsRepository extends CrudRepository<PartEntity, Long> {
}
