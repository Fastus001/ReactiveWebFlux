package pl.fastus.reactive.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pl.fastus.reactive.domain.Profile;
import pl.fastus.reactive.events.ProfileCreatedEvent;
import pl.fastus.reactive.repositories.ProfileRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
public class ProfileService {

    private final ApplicationEventPublisher publisher;
    private final ProfileRepository profileRepository;

    public ProfileService(ApplicationEventPublisher publisher, ProfileRepository profileRepository) {
        this.publisher = publisher;
        this.profileRepository = profileRepository;
    }

    public Flux<Profile> all(){
        return profileRepository.findAll();
    }

    public Mono<Profile> get(String id){
        return profileRepository.findById( id );
    }

    public Mono<Profile> update(String id, String email){
        return profileRepository.findById( id )
                .map( profile -> new Profile( profile.getId(), email) )
                .flatMap( profileRepository::save );
    }

    public Mono<Profile> delete(String id){
        return profileRepository.findById( id )
                .flatMap( p->profileRepository.deleteById( p.getId() ).thenReturn( p )  );
    }

    public Mono<Profile> create(String email){
        return profileRepository.save( new Profile(null, email) )
                .doOnSuccess( profile -> publisher.publishEvent( new ProfileCreatedEvent( profile) ) );
    }
}
