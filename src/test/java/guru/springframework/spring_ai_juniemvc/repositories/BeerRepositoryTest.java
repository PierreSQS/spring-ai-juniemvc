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
    void basicCrudOperations() {
        // Create
        Beer saved = beerRepository.save(getSampleBeer());
        assertThat(saved.getId()).as("ID should be generated").isNotNull();

        // Read
        Optional<Beer> fetchedOpt = beerRepository.findById(saved.getId());
        assertThat(fetchedOpt).isPresent();
        Beer fetched = fetchedOpt.get();
        assertThat(fetched.getBeerName()).isEqualTo("Galaxy Cat");
        assertThat(fetched.getVersion()).as("Version should be initialized by JPA").isNotNull();

        // Update
        fetched.setQuantityOnHand(20);
        fetched.setPrice(new BigDecimal("10.49"));
        Beer updated = beerRepository.save(fetched);
        assertThat(updated.getQuantityOnHand()).isEqualTo(20);
        assertThat(updated.getPrice()).isEqualByComparingTo("10.49");

        // Count / findAll
        assertThat(beerRepository.count()).isEqualTo(1);
        assertThat(beerRepository.findAll()).hasSize(1);

        // Delete
        beerRepository.deleteById(updated.getId());
        assertThat(beerRepository.findById(updated.getId())).isEmpty();
        assertThat(beerRepository.count()).isZero();
    }
}
