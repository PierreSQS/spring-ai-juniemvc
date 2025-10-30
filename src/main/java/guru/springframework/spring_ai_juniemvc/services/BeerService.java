package guru.springframework.spring_ai_juniemvc.services;

import guru.springframework.spring_ai_juniemvc.entities.Beer;

import java.util.List;
import java.util.Optional;

public interface BeerService {

    Beer create(Beer beer);

    Optional<Beer> findById(Integer id);

    List<Beer> findAll();

    boolean updateById(Integer id, Beer beer);

    boolean deleteById(Integer id);
}
