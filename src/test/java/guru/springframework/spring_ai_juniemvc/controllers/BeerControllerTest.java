package guru.springframework.spring_ai_juniemvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring_ai_juniemvc.entities.Beer;
import guru.springframework.spring_ai_juniemvc.services.BeerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    BeerService beerService;

    private Beer getSampleBeer(Integer id) {
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
    @DisplayName("POST /api/v1/beer should create and return 201 with Location header")
    void createBeer_returns201AndLocation() throws Exception {
        Beer toCreate = getSampleBeer(null);
        Beer saved = getSampleBeer(1);
        Mockito.when(beerService.create(any(Beer.class))).thenReturn(saved);

        mockMvc.perform(post("/api/v1/beer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toCreate)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/api/v1/beer/1")))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.beerName", is("Galaxy Cat")));
    }

    @Test
    @DisplayName("GET /api/v1/beer/{id} when found returns 200 and body")
    void getById_found_returns200() throws Exception {
        Mockito.when(beerService.findById(eq(1))).thenReturn(Optional.of(getSampleBeer(1)));

        mockMvc.perform(get("/api/v1/beer/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.beerName", is("Galaxy Cat")));
    }

    @Test
    @DisplayName("GET /api/v1/beer/{id} when not found returns 404")
    void getById_notFound_returns404() throws Exception {
        Mockito.when(beerService.findById(eq(99))).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/beer/{id}", 99))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/v1/beer returns list of beers")
    void listAll_returnsArray() throws Exception {
        Mockito.when(beerService.findAll()).thenReturn(List.of(getSampleBeer(1), getSampleBeer(2)));

        mockMvc.perform(get("/api/v1/beer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }
}
