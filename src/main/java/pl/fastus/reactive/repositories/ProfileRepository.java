package pl.fastus.reactive.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pl.fastus.reactive.domain.Profile;

public interface ProfileRepository extends ReactiveMongoRepository<Profile, String> {

}
