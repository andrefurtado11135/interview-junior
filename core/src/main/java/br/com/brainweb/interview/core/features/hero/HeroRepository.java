package br.com.brainweb.interview.core.features.hero;

import br.com.brainweb.interview.model.Hero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface HeroRepository extends JpaRepository<Hero, UUID> {

    @Query("SELECT h FROM Hero h WHERE h.name = :name")
    Optional<Hero> getHeroByName(@Param("name") String name);

}
