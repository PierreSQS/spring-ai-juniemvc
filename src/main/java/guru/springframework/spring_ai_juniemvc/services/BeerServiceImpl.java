package guru.springframework.spring_ai_juniemvc.services;

import guru.springframework.spring_ai_juniemvc.entities.Beer;
import guru.springframework.spring_ai_juniemvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;

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

    @Override
    public boolean updateById(Integer id, Beer beer) {
        return beerRepository.findById(id).map(existing -> {
            existing.setBeerName(beer.getBeerName());
            existing.setBeerStyle(beer.getBeerStyle());
            existing.setUpc(beer.getUpc());
            existing.setQuantityOnHand(beer.getQuantityOnHand());
            existing.setPrice(beer.getPrice());
            beerRepository.save(existing);
            return true;
        }).orElse(false);
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
