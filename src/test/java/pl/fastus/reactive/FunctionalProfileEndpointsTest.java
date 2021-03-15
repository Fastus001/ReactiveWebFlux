package pl.fastus.reactive;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.fastus.reactive.config.ProfileEndpointConfiguration;
import pl.fastus.reactive.controller.ProfileRestController;
import pl.fastus.reactive.handlers.ProfileHandler;
import pl.fastus.reactive.services.ProfileService;

@Log4j2
@ActiveProfiles("default")
@Import( {ProfileEndpointConfiguration.class, ProfileHandler.class,
        ProfileService.class} )
public class FunctionalProfileEndpointsTest extends AbstractBaseProfileEndpointsTest{

    @BeforeAll
    static void before(){
        log.info( "running default "+ ProfileRestController.class.getName() + " tests" );
    }

    public FunctionalProfileEndpointsTest(@Autowired WebTestClient client) {
        super( client );
    }
}
