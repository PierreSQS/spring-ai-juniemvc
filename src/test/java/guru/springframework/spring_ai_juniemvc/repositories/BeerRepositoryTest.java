package guru.springframework.spring_ai_juniemvc.repositories;

import guru.springframework.spring_ai_juniemvc.entities.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    private Beer getSampleBeer() {
        return Beer.builder()
                .beerName("Galaxy Cat")
                .beerStyle("IPA")
                .upc("12345")
                .quantityOnHand(10)
                .price(new BigDecimal("9.99"))
                .build();
    }

    @Test
    void createBeer_savesAndAssignsId() {
        Beer saved = beerRepository.save(getSampleBeer());
        assertThat(saved.getId()).as("ID should be generated").isNotNull();
        assertThat(saved.getVersion()).as("Version should be initialized by JPA").isNotNull();
    }

    @Test
    void readBeer_findsById() {
        Beer saved = beerRepository.save(getSampleBeer());
        Optional<Beer> fetchedOpt = beerRepository.findById(saved.getId());
        assertThat(fetchedOpt).isPresent();
        Beer fetched = fetchedOpt.get();
        assertThat(fetched.getBeerName()).isEqualTo("Galaxy Cat");
    }

    @Test
    void updateBeer_updatesFields() {
        Beer saved = beerRepository.save(getSampleBeer());
        saved.setQuantityOnHand(20);
        saved.setPrice(new BigDecimal("10.49"));
        Beer updated = beerRepository.save(saved);
        assertThat(updated.getQuantityOnHand()).isEqualTo(20);
        assertThat(updated.getPrice()).isEqualByComparingTo("10.49");
    }

    @Test
    void deleteBeer_removesEntity() {
        Beer saved = beerRepository.save(getSampleBeer());
        Integer id = saved.getId();
        beerRepository.deleteById(id);
        assertThat(beerRepository.findById(id)).isEmpty();
    }
}
