package pl.fastus.reactive.controller;

import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.fastus.reactive.domain.Profile;
import pl.fastus.reactive.services.ProfileService;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping(value = "/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileRestController {

    private final MediaType mediaType = MediaType.APPLICATION_JSON;
    private final ProfileService profileService;

    public ProfileRestController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    Publisher<Profile> getAll(){
        return profileService.all();
    }

    @GetMapping("/{id}")
    Publisher<Profile> getById(@PathVariable("id") String  id){
        return profileService.get( id );
    }

    @PostMapping
    Publisher<ResponseEntity<Profile>> create(@RequestBody Profile profile){
        return profileService.create( profile.getEmail() )
                .map( p->ResponseEntity.created( URI.create( "/profiles/"+p.getId() ) )
                .contentType( mediaType )
                .build());
    }

    @DeleteMapping("/{id}")
    Publisher<Profile> deleteById(@PathVariable("id") String id){
        return profileService.delete( id );
    }

    @PutMapping("/{id}")
    Publisher<ResponseEntity<Profile>> updateById(@PathVariable("id") String id, @RequestBody Profile profile){
        return Mono
                .just( profile )
                .flatMap( p->profileService.update( id, profile.getEmail() ) )
                .map( p->ResponseEntity
                .ok()
                .contentType( mediaType )
                .build()) ;
    }
}
