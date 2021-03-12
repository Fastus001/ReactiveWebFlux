package pl.fastus.reactive.services;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import pl.fastus.reactive.domain.Profile;
import pl.fastus.reactive.repositories.ProfileRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;
import java.util.function.Predicate;

@Log4j2
@DataMongoTest
@Import( ProfileService.class )
class ProfileServiceTest {

    private final ProfileService service;

    private final ProfileRepository repository;

    public ProfileServiceTest(@Autowired ProfileService service, @Autowired ProfileRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @BeforeEach
    void setup(){
    }

    @Test
    public void getAll() {
        Flux<Profile> saved = repository.saveAll(Flux.just(new Profile(null, "Josh"), new Profile(null, "Matt"), new Profile(null, "Jane")));
        Flux<Profile> composite = service.all().thenMany(saved);

        Predicate<Profile> match = profile -> saved.any(saveItem -> saveItem.equals(profile)).block();

        StepVerifier
                .create(composite)
                .expectNextMatches(match)
                .expectNextMatches(match)
                .expectNextMatches(match);
//                .verifyComplete();
    }

    @Test
    void create() {
        final Mono<Profile> profileMono = service.create( "email@email.com" );

        StepVerifier
                .create( profileMono )
                .expectNextMatches( saved -> StringUtils.hasText(saved.getId()) )
                .verifyComplete();
    }

    @Test
    void delete() {
        String test = "test";
        final Mono<Profile> deleted = service
                .create( test )
                .flatMap( saved -> service.delete( saved.getId() ) );
        StepVerifier
                .create( deleted )
                .expectNextMatches( profile -> profile.getEmail().equalsIgnoreCase( "test" ) )
                .verifyComplete();


    }

    @Test
    void update() {
        Mono<Profile> saved = this.service
                .create("test")
                .flatMap(p -> this.service.update(p.getId(), "test1"));
        StepVerifier
                .create(saved)
                .expectNextMatches(p -> p.getEmail().equalsIgnoreCase("test1"))
                .verifyComplete();
    }

    @Test
    void getById() {
        String test = UUID.randomUUID().toString();
        Mono<Profile> deleted = this.service
                .create(test)
                .flatMap(saved -> this.service.get(saved.getId()));
        StepVerifier
                .create(deleted)
                .expectNextMatches(profile -> StringUtils.hasText(profile.getId()) && test.equalsIgnoreCase(profile.getEmail()))
                .verifyComplete();
    }
}