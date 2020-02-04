package br.com.softdesign.career.votingservice.configuration;

import br.com.softdesign.career.votingservice.handler.VotingAgendaHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class VotingAgendaRouterConfiguration {

    @Bean
    public RouterFunction<ServerResponse> route(final VotingAgendaHandler handler) {
        return RouterFunctions
                .route(POST(VotingAgendaHandler.URL_PATTERN)
                                .and(accept(APPLICATION_JSON))
                                .and(contentType(APPLICATION_JSON)),
                        handler::createVotingAgenda);
    }

}
