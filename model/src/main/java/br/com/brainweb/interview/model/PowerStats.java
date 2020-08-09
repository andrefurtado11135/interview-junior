package br.com.brainweb.interview.model;

import br.com.brainweb.interview.model.request.CreateHeroRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PowerStats {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private int strength;

    private int agility;

    private int dexterity;

    private int intelligence;

    private Instant createdAt;
    private Instant updatedAt;

    public PowerStats(CreateHeroRequest createHeroRequest) {
        this.strength = createHeroRequest.getStrength();
        this.agility = createHeroRequest.getAgility();
        this.dexterity = createHeroRequest.getDexterity();
        this.intelligence = createHeroRequest.getIntelligence();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

}
