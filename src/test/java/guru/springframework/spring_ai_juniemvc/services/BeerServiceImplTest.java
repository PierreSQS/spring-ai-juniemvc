package guru.springframework.spring_ai_juniemvc.services;

import guru.springframework.spring_ai_juniemvc.entities.Beer;
import guru.springframework.spring_ai_juniemvc.repositories.BeerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BeerServiceImplTest {

    @Mock
    BeerRepository beerRepository;

    @InjectMocks
    BeerServiceImpl beerService;

    private Beer sample(Integer id) {
        return Beer.builder()
                .id(id)
                .beerName("Galaxy Cat")
                .beerStyle("IPA")
                .upc("12345")
                .quantityOnHand(10)
                .price(new BigDecimal("9.99"))
                .build();
    }

    @Test
    @DisplayName("create should save and return entity")
    void create() {
        Beer toSave = sample(null);
        Beer saved = sample(1);
        given(beerRepository.save(any(Beer.class))).willReturn(saved);

        Beer result = beerService.create(toSave);

        assertThat(result.getId()).isEqualTo(1);
        verify(beerRepository).save(any(Beer.class));
    }

    @Test
    @DisplayName("findById when present returns Optional.of")
    void findById_found() {
        given(beerRepository.findById(eq(1))).willReturn(Optional.of(sample(1)));
        Optional<Beer> result = beerService.findById(1);
        assertThat(result).isPresent();
    }

    @Test
    @DisplayName("findById when absent returns Optional.empty")
    void findById_notFound() {
        given(beerRepository.findById(eq(99))).willReturn(Optional.empty());
        Optional<Beer> result = beerService.findById(99);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findAll returns list from repository")
    void findAll() {
        given(beerRepository.findAll()).willReturn(List.of(sample(1), sample(2)));
        List<Beer> list = beerService.findAll();
        assertThat(list).hasSize(2);
    }

    @Test
    @DisplayName("updateById when found updates and returns true")
    void updateById_found() {
        Beer existing = sample(1);
        Beer update = sample(null);
        update.setBeerName("New Name");
        given(beerRepository.findById(eq(1))).willReturn(Optional.of(existing));
        given(beerRepository.save(any(Beer.class))).willAnswer(invocation -> invocation.getArgument(0));

        boolean updated = beerService.updateById(1, update);

        assertThat(updated).isTrue();
        assertThat(existing.getBeerName()).isEqualTo("New Name");
        verify(beerRepository).save(any(Beer.class));
    }

    @Test
    @DisplayName("updateById when not found returns false")
    void updateById_notFound() {
        given(beerRepository.findById(eq(99))).willReturn(Optional.empty());
        boolean updated = beerService.updateById(99, sample(null));
        assertThat(updated).isFalse();
    }

    @Test
    @DisplayName("deleteById when exists returns true and deletes")
    void deleteById_found() {
        given(beerRepository.existsById(eq(1))).willReturn(true);
        boolean deleted = beerService.deleteById(1);
        assertThat(deleted).isTrue();
        verify(beerRepository).deleteById(1);
    }

    @Test
    @DisplayName("deleteById when missing returns false")
    void deleteById_notFound() {
        given(beerRepository.existsById(eq(99))).willReturn(false);
        boolean deleted = beerService.deleteById(99);
        assertThat(deleted).isFalse();
    }
}
