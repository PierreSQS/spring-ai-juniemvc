package guru.springframework.spring_ai_juniemvc.services;

import guru.springframework.spring_ai_juniemvc.entities.Beer;
import guru.springframework.spring_ai_juniemvc.repositories.BeerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;

    public BeerServiceImpl(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    @Override
    public Beer create(Beer beer) {
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
}
