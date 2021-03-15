package pl.fastus.reactive;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.fastus.reactive.controller.ProfileRestController;
import pl.fastus.reactive.services.ProfileService;

@Log4j2
@Import( {ProfileRestController.class, ProfileService.class} )
@ActiveProfiles("classic")
public class ClassicProfileEndpointsTest extends AbstractBaseProfileEndpointsTest{

    @BeforeAll
    static void before(){
        log.info( "running non-classic tests" );
    }

    public ClassicProfileEndpointsTest(@Autowired WebTestClient client) {
        super( client );
    }
}
