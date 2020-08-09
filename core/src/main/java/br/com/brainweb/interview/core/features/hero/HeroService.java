package br.com.brainweb.interview.core.features.hero;

import br.com.brainweb.interview.core.features.powerstats.PowerStatsRepository;
import br.com.brainweb.interview.model.ComparisonDto;
import br.com.brainweb.interview.model.Hero;
import br.com.brainweb.interview.model.HeroDto;
import br.com.brainweb.interview.model.PowerStats;
import br.com.brainweb.interview.model.enums.Race;
import br.com.brainweb.interview.model.request.CreateHeroRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class HeroService {

    @Autowired
    private HeroRepository heroRepository;

    @Autowired
    private PowerStatsRepository powerStatsRepository;

    @Transactional
    public UUID create(CreateHeroRequest createHeroRequest) {
        try{
            Hero hero = new Hero(createHeroRequest);
            PowerStats powerStatus = new PowerStats(createHeroRequest);
            PowerStats powerStatusPersisted = powerStatsRepository.save(powerStatus);
            hero.setPowerStats(powerStatusPersisted);
            heroRepository.save(hero);
            log.info("HeroService - create() : Hero successfully created");
            return hero.getId();
        }catch(Throwable throwable){
            log.error("ERROR - HeroService - create() : " + throwable.getStackTrace());
            throw throwable;
        }
    }

    public HeroDto getById(UUID id){
        try{
            Hero heroBd = heroRepository.getOne(id);
            return heroToDto(heroBd);
        }catch(EntityNotFoundException ex){
            log.info("HeroService - getById() : Not found entity with ID: " + id);
            throw ex;
        }catch (Throwable throwable){
            log.info("ERROR - HeroService - getById() : " + throwable.getStackTrace());
            throw throwable;
        }
    }

    public HeroDto getByName(String name){
        try{
            Optional<Hero> hero = heroRepository.getHeroByName(name);
            if (hero.isPresent()){
                return heroToDto(hero.get());
            }else{
                throw new EntityNotFoundException();
            }
        }catch(EntityNotFoundException ex){
            log.info("HeroService - getByName() : Not found entity with Name: " + name);
            throw ex;
        }catch(Throwable throwable){
            log.info("ERROR - HeroService - getByName() : " + throwable.getStackTrace());
            throw throwable;
        }
    }

    public void deleteHeroById(UUID id){
        try{
            Hero heroBd = heroRepository.getOne(id);
            PowerStats powerStatsBd = heroBd.getPowerStats();
            heroRepository.delete(heroBd);
            powerStatsRepository.delete(powerStatsBd);
            log.info("HeroService - deleteHeroById() : Hero with ID " + id + " successfully deleted");
        }catch(EntityNotFoundException ex){
            log.info("HeroService - deleteHeroById() : Not found entity with ID: " + id);
            throw ex;
        }catch (Throwable throwable){
            log.info("ERROR - HeroService - deleteHeroById() : " + throwable.getStackTrace());
            throw throwable;
        }
    }

    public void updateHeroById(UUID id, HeroDto heroDto){
        try{
            Hero heroBd = heroRepository.getOne(id);
            PowerStats powerStatsBd = heroBd.getPowerStats();
            checkUpdate(heroBd, powerStatsBd, heroDto);
            log.info("HeroService - updateHeroById() : Hero with ID " + id + " successfully updated");
        }catch(EntityNotFoundException ex) {
            log.info("HeroService - updateHeroById() : Not found entity with ID: " + id);
            throw ex;
        }catch (Throwable throwable){
            log.info("ERROR - HeroService - deleteHeroById() : " + throwable.getStackTrace());
            throw throwable;
        }
    }

    private HeroDto heroToDto(Hero hero){
        HeroDto heroDto = new HeroDto();
        PowerStats powerStatsBd = hero.getPowerStats();
        heroDto.setHeroId(hero.getId());
        heroDto.setName(hero.getName());
        heroDto.setRace(hero.getRace().toString());
        heroDto.setAgility(powerStatsBd.getAgility());
        heroDto.setDexterity(powerStatsBd.getDexterity());
        heroDto.setStrength(powerStatsBd.getStrength());
        heroDto.setIntelligence(powerStatsBd.getIntelligence());
        return heroDto;
    }

    private void checkUpdate(Hero hero, PowerStats powerStats, HeroDto heroDto){
        if (heroDto.getName() != null && !heroDto.getName().isEmpty()){
            hero.setName(heroDto.getName());
        }
        if (heroDto.getRace() != null && !heroDto.getName().isEmpty()){
            hero.setRace(Race.getByValue(heroDto.getRace()));
        }
        if (heroDto.getStrength() != null){
            powerStats.setStrength(heroDto.getStrength());
        }
        if (heroDto.getAgility() != null){
            powerStats.setAgility(heroDto.getAgility());
        }
        if (heroDto.getIntelligence() != null){
            powerStats.setIntelligence(heroDto.getIntelligence());
        }
        if (heroDto.getDexterity() != null){
            powerStats.setDexterity(heroDto.getDexterity());
        }
        powerStats.setUpdatedAt(Instant.now());
        hero.setUpdatedAt(Instant.now());
        powerStatsRepository.save(powerStats);
        heroRepository.save(hero);
    }

    public List<ComparisonDto> compareHeroes(String heroName1, String heroName2){
        try{
            Optional<Hero> hero1 = heroRepository.getHeroByName(heroName1);
            Optional<Hero> hero2 = heroRepository.getHeroByName(heroName2);
            if (hero1.isPresent() && hero2.isPresent()){
                return compareStats(hero1.get(), hero2.get());
            }else{
                throw new EntityNotFoundException();
            }
        }catch (EntityNotFoundException ex){
            log.info("HeroService - compareHeroes() : Hero(es) to compare not found");
            throw ex;
        }catch (Throwable throwable){
            log.info("ERROR - HeroService - compareHeroes() : " + throwable.getStackTrace());
            throw throwable;
        }
    }

    private List<ComparisonDto> compareStats(Hero hero1, Hero hero2){
        List<ComparisonDto> comparisonList = new ArrayList<>();
        ComparisonDto comparisonDto1 = new ComparisonDto();
        ComparisonDto comparisonDto2 = new ComparisonDto();
        comparisonDto1.setId(hero1.getId());
        comparisonDto2.setId(hero2.getId());
        comparisonDto1.setStrengthDiff(difference(hero1.getPowerStats().getStrength(), hero2.getPowerStats().getStrength()));
        comparisonDto1.setAgilityDiff(difference(hero1.getPowerStats().getAgility(), hero2.getPowerStats().getAgility()));
        comparisonDto1.setDexterityDiff(difference(hero1.getPowerStats().getDexterity(), hero2.getPowerStats().getDexterity()));
        comparisonDto1.setIntelligenceDiff(difference(hero1.getPowerStats().getIntelligence(), hero2.getPowerStats().getIntelligence()));
        comparisonDto2.setStrengthDiff(difference(hero2.getPowerStats().getStrength(), hero1.getPowerStats().getStrength()));
        comparisonDto2.setAgilityDiff(difference(hero2.getPowerStats().getAgility(), hero1.getPowerStats().getAgility()));
        comparisonDto2.setDexterityDiff(difference(hero2.getPowerStats().getDexterity(), hero1.getPowerStats().getDexterity()));
        comparisonDto2.setIntelligenceDiff(difference(hero2.getPowerStats().getIntelligence(), hero1.getPowerStats().getIntelligence()));
        comparisonList.add(comparisonDto1);
        comparisonList.add(comparisonDto2);
        return comparisonList;
    }

    private String difference(int stats1, int stats2){
        Integer diff = stats1 - stats2;
        if (diff > 0){
            return "+".concat(diff.toString());
        }
        return diff.toString();
    }

}
