package br.com.softdesign.career.votingservice.handler;

import br.com.softdesign.career.votingservice.mapper.VotingAgendaMapper;
import br.com.softdesign.career.votingservice.service.VotingAgendaService;
import br.com.softdesign.career.votingservice.to.VotingAgendaTO;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class VotingAgendaHandler {

    public static final String URL_PATTERN = "/voting-agenda";

    private final VotingAgendaService service;

    public VotingAgendaHandler(final VotingAgendaService service) {
        this.service = service;
    }

    public Mono<ServerResponse> createVotingAgenda(final ServerRequest serverRequest) {
        return serverRequest.bodyToMono(VotingAgendaTO.class)
                .map(VotingAgendaMapper::toModel)
                .flatMap(service::createVotingAgenda)
                .flatMap(votingAgenda -> ServerResponse
                        .created(URI.create(String.join("/", URL_PATTERN, votingAgenda.getId())))
                        .build());
    }

}
