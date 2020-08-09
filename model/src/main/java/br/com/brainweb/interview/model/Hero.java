package br.com.brainweb.interview.model;

import br.com.brainweb.interview.model.enums.Race;
import br.com.brainweb.interview.model.request.CreateHeroRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Hero {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Race race;

    @OneToOne
    private PowerStats powerStats;

    private Instant createdAt;

    private Instant updatedAt;

    private boolean enabled;

    public Hero(CreateHeroRequest createHeroRequest) {
        this.name = createHeroRequest.getName();
        this.race = createHeroRequest.getRace();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.enabled = true;
    }
}
