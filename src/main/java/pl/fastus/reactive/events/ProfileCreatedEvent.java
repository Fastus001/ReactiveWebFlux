package pl.fastus.reactive.events;

import org.springframework.context.ApplicationEvent;
import pl.fastus.reactive.domain.Profile;

public class ProfileCreatedEvent extends ApplicationEvent {


    public ProfileCreatedEvent(Profile source) {
        super( source );
    }
}
