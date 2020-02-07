package br.com.softdesign.career.votingservice.controller;

import br.com.softdesign.career.votingservice.mapper.VotingAgendaMapper;
import br.com.softdesign.career.votingservice.service.VotingAgendaService;
import br.com.softdesign.career.votingservice.to.VotingAgendaTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class VotingAgendaController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static final String URL_PATTERN = "/voting-agenda";

    private final VotingAgendaService service;

    private final VotingAgendaMapper mapper;

    public VotingAgendaController(final VotingAgendaService service, final VotingAgendaMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @ApiOperation(value = "Criar pauta", notes = "Operação para criar uma pauta para votação na assembleia")
    @PostMapping(VotingAgendaController.URL_PATTERN)
    public Mono<ResponseEntity<?>> createVotingAgenda(
            @ApiParam("Dados de entrada da pauta")
            @Valid @RequestBody final VotingAgendaTO votingAgendaTO) {
        log.debug("Received data for creating agenda with title {}", votingAgendaTO.getTitle());
        return service.createVotingAgenda(mapper.map(votingAgendaTO))
                .map(votingAgenda -> ResponseEntity
                        .created(URI.create(URL_PATTERN + "/" + votingAgenda.getId()))
                        .build());
    }

}
