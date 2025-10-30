package guru.springframework.spring_ai_juniemvc.services;

import guru.springframework.spring_ai_juniemvc.entities.Beer;

import java.util.List;
import java.util.Optional;

/**
 * Simplified service API for Beer aggregate.
 * <p>
 * We intentionally expose only one write-operation: create (Beer).
 * With Spring Data JPA, repository.save(entity) will:
 *  - INSERT a new row when the entity id is null
 *  - UPDATE an existing row when the entity id is set to an existing id
 * <p>
 * Therefore, callers can perform both create and update by calling create(beer)
 * and controlling whether beer.getId() is null (create) or has a value (update).
 */
public interface BeerService {

    /**
     * Create a new Beer or update an existing one when id is present.
     */
    Beer create(Beer beer);

    /**
     * Find a Beer by its primary key.
     */
    Optional<Beer> findById(Integer id);

    /**
     * Return all Beers.
     */
    List<Beer> findAll();

    /**
     * Delete Beer by id. Returns true when a row was deleted, false if id not found.
     */
    boolean deleteById(Integer id);
}
