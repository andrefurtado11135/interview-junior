package br.com.brainweb.interview.core.features.hero;

import br.com.brainweb.interview.model.HeroDto;
import br.com.brainweb.interview.model.request.CreateHeroRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.net.URI;
import java.util.UUID;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/heroes", produces = APPLICATION_JSON_VALUE)
public class HeroController {

    private final HeroService heroService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity create(@Validated @RequestBody CreateHeroRequest createHeroRequest) {
       try{
           final UUID id = heroService.create(createHeroRequest);
           return created(URI.create(format("/api/v1/heroes/%s", id))).build();
       }catch (DataIntegrityViolationException ex){
           return ResponseEntity.badRequest().body(ex.toString());
       }
    }

    @GetMapping("/{id}")
    public ResponseEntity getHero(@PathVariable("id") UUID id) {
        if (id != null){
            try{
                HeroDto hero = heroService.getById(id);
                return ResponseEntity.ok(hero);
            }catch(EntityNotFoundException ex){
                return ResponseEntity.notFound().build();
            }
        }else{
            return ResponseEntity.badRequest().body("ERROR : id is null");
        }
    }

    @GetMapping
    public ResponseEntity getHeroByName(@RequestParam("name") String name) {
        if (name == null || name.isEmpty()){
            return ResponseEntity.badRequest().body("ERROR : Name can't be null or empty");
        }else{
            try{
                HeroDto hero = heroService.getByName(name);
                return ResponseEntity.ok(hero);
            }catch(EntityNotFoundException ex){
                return ResponseEntity.ok().build();
            }
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteHeroById(@PathVariable("id") UUID id){
        if (id != null){
            try{
                heroService.deleteHeroById(id);
                return ResponseEntity.ok().body("Hero successfully deleted");
            }catch(EntityNotFoundException ex){
                return ResponseEntity.notFound().build();
            }
        }else{
            return ResponseEntity.badRequest().body("ERROR : id is null");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateHeroById(@PathVariable("id") UUID id, @RequestBody HeroDto heroDto){
        if (id != null && heroDto != null){
            try{
                heroService.updateHeroById(id, heroDto);
                return ResponseEntity.ok().body("Hero successfully updated");
            }catch(EntityNotFoundException ex){
                return ResponseEntity.notFound().build();
            }
        }else{
            return ResponseEntity.badRequest().body("ERROR : id/body is null");
        }
    }

    @GetMapping("/compare")
    public ResponseEntity compareHeroes(@RequestParam("hero1") String heroName1, @RequestParam("hero2") String heroName2){
        if (heroName1 != null && !heroName1.isEmpty() && heroName2 != null && !heroName2.isEmpty()){
            try{
                return ResponseEntity.ok(heroService.compareHeroes(heroName1, heroName2));
            }catch(EntityNotFoundException ex){
                return ResponseEntity.status(404).body("Hero(es) to compare not found");
            }
        }else{
            return ResponseEntity.badRequest().body("ERROR : parameters are null/empty");
        }
    }
}
