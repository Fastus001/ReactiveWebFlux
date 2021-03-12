package pl.fastus.reactive.predicates;

import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.ServerRequest;

public class CaseInsensitiveRequestPredicate implements RequestPredicate {

    private final RequestPredicate target;

    public CaseInsensitiveRequestPredicate(RequestPredicate target) {
        this.target = target;
    }

    @Override
    public boolean test(ServerRequest request) {
        return target.test( new LowerCaseUriServerRequestWrapper(request) );
    }

    @Override
    public String toString() {
        return target.toString();
    }
}
