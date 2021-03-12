package pl.fastus.reactive.handlers;

import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pl.fastus.reactive.domain.Profile;
import pl.fastus.reactive.services.ProfileService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class ProfileHandler {

    private final ProfileService profileService;

    public ProfileHandler(ProfileService profileService) {
        this.profileService = profileService;
    }


    public Mono<ServerResponse> getById(ServerRequest request){
        return defaultReadResponse( profileService.get( id(request) ) );
    }

    public Mono<ServerResponse> all(ServerRequest request) {
        return defaultReadResponse( profileService.all() );
    }

    public Mono<ServerResponse> deleteById(ServerRequest request){
        return defaultReadResponse( profileService.delete( id(request) ) );
    }

    public Mono<ServerResponse> updateById(ServerRequest request){
        Flux<Profile> id = request.bodyToFlux(Profile.class)
                .flatMap( p->profileService.update( id( request ),p.getEmail() ) );
        return defaultReadResponse( id );
    }

    public Mono<ServerResponse> create(ServerRequest request){
        final Flux<Profile> profileFlux = request.bodyToFlux( Profile.class )
                .flatMap( p -> profileService.create( p.getEmail() ) );
        return defaultWriteResponse( profileFlux );
    }

    private static Mono<ServerResponse> defaultWriteResponse(Publisher<Profile> profiles) {
        return Mono
                .from(profiles)
                .flatMap(p -> ServerResponse
                        .created( URI.create( "/profiles/" + p.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .build()
                );
    }

    private static Mono<ServerResponse> defaultReadResponse(Publisher<Profile> profiles){
        return ServerResponse
                .ok()
                .contentType( MediaType.APPLICATION_JSON )
                .body( profiles, Profile.class );
    }

    private static String id(ServerRequest request){
        return request.pathVariable("id");
    }
}
