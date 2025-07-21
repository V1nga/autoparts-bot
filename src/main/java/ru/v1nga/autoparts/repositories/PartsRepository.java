package ru.v1nga.autoparts.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.v1nga.autoparts.entities.PartEntity;

import java.util.List;
import java.util.Optional;

public interface PartsRepository extends CrudRepository<PartEntity, Long> {

    Optional<PartEntity> findByNumber(String partNumber);

    @Query(
        value = "SELECT * FROM car_parts WHERE number_vector @@ plainto_tsquery('simple', :query)",
        nativeQuery = true
    )
    List<PartEntity> searchPart(@Param("query") String partNumber);
}
