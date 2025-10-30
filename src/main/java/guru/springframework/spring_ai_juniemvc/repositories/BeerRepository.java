package guru.springframework.spring_ai_juniemvc.repositories;

import guru.springframework.spring_ai_juniemvc.entities.Beer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeerRepository extends JpaRepository<Beer, Integer> {
}
