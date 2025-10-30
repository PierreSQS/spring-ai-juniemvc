package guru.springframework.spring_ai_juniemvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring_ai_juniemvc.entities.Beer;
import guru.springframework.spring_ai_juniemvc.services.BeerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
        given(beerService.create(any(Beer.class))).willReturn(saved);

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
        given(beerService.findById(eq(1))).willReturn(Optional.of(getSampleBeer(1)));

        mockMvc.perform(get("/api/v1/beer/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.beerName", is("Galaxy Cat")));
    }

    @Test
    @DisplayName("GET /api/v1/beer/{id} when not found returns 404")
    void getById_notFound_returns404() throws Exception {
        given(beerService.findById(eq(99))).willReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/beer/{id}", 99))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/v1/beer returns list of beers")
    void listAll_returnsArray() throws Exception {
        given(beerService.findAll()).willReturn(List.of(getSampleBeer(1), getSampleBeer(2)));

        mockMvc.perform(get("/api/v1/beer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andDo(print());
    }

    @Test
    @DisplayName("PUT /api/v1/beer/{id} when found returns 204")
    void update_found_returns204() throws Exception {
        Beer update = getSampleBeer(null);
        // Controller now checks existence, then calls create() to perform update
        given(beerService.findById(eq(1))).willReturn(Optional.of(getSampleBeer(1)));
        given(beerService.create(any(Beer.class))).willReturn(getSampleBeer(1));

        mockMvc.perform(put("/api/v1/beer/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @DisplayName("PUT /api/v1/beer/{id} when not found returns 404")
    void update_notFound_returns404() throws Exception {
        Beer update = getSampleBeer(null);
        given(beerService.findById(eq(99))).willReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/beer/{id}", 99)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/v1/beer/{id} when found returns 204")
    void delete_found_returns204() throws Exception {
        given(beerService.deleteById(eq(1))).willReturn(true);

        mockMvc.perform(delete("/api/v1/beer/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/v1/beer/{id} when not found returns 404")
    void delete_notFound_returns404() throws Exception {
        given(beerService.deleteById(eq(99))).willReturn(false);

        mockMvc.perform(delete("/api/v1/beer/{id}", 99))
                .andExpect(status().isNotFound());
    }
}
