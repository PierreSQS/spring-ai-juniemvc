package guru.springframework.spring_ai_juniemvc.services;

import guru.springframework.spring_ai_juniemvc.entities.Beer;
import guru.springframework.spring_ai_juniemvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation using a single write operation: create(Beer).
 *
 * Spring Data JPA's save(entity) handles both insert and update based on id:
 *  - If beer.getId() is null -> INSERT
 *  - If beer.getId() is not null and exists -> UPDATE
 */
@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;

    @Override
    public Beer create(Beer beer) {
        // Delegate to repository.save which performs insert or update depending on id
        return beerRepository.save(beer);
    }

    @Override
    public Optional<Beer> findById(Integer id) {
        return beerRepository.findById(id);
    }

    @Override
    public List<Beer> findAll() {
        return beerRepository.findAll();
    }

    @Override
    public boolean deleteById(Integer id) {
        if (beerRepository.existsById(id)) {
            beerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
