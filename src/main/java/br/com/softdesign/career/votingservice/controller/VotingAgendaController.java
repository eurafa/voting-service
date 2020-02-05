package br.com.softdesign.career.votingservice.controller;

import br.com.softdesign.career.votingservice.mapper.VotingAgendaMapper;
import br.com.softdesign.career.votingservice.service.VotingAgendaService;
import br.com.softdesign.career.votingservice.to.VotingAgendaTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = VotingAgendaController.URL_PATTERN)
public class VotingAgendaController {

    public static final String URL_PATTERN = "/voting-agenda";

    private final VotingAgendaService service;

    public VotingAgendaController(final VotingAgendaService service) {
        this.service = service;
    }

    @PostMapping
    public Mono<ResponseEntity<?>> createVotingAgenda(@Valid @RequestBody final VotingAgendaTO votingAgendaTO) {
        return service.createVotingAgenda(VotingAgendaMapper.toModel(votingAgendaTO))
                .map(votingAgenda -> ResponseEntity
                        .created(URI.create(URL_PATTERN + "/" + votingAgenda.getId()))
                        .build());
    }

}
