package br.com.softdesign.career.votingservice.controller;

import br.com.softdesign.career.votingservice.mapper.MemberVoteMapper;
import br.com.softdesign.career.votingservice.mapper.ResultsTOMapper;
import br.com.softdesign.career.votingservice.mapper.VotingSessionMapper;
import br.com.softdesign.career.votingservice.service.VotingSessionResultService;
import br.com.softdesign.career.votingservice.service.VotingSessionService;
import br.com.softdesign.career.votingservice.to.MemberVoteTO;
import br.com.softdesign.career.votingservice.to.OpenVotingSessionTO;
import br.com.softdesign.career.votingservice.to.ResultsTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class VotingSessionController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static final String URL_PATTERN = "/voting-session";

    private final VotingSessionService service;

    private final VotingSessionResultService resultService;

    private final VotingSessionMapper votingSessionMapper;

    private final MemberVoteMapper memberVoteMapper;

    private final ResultsTOMapper resultsTOMapper;

    public VotingSessionController(final VotingSessionService service,
                                   final VotingSessionResultService resultService,
                                   final VotingSessionMapper votingSessionMapper,
                                   final MemberVoteMapper memberVoteMapper,
                                   final ResultsTOMapper resultsTOMapper) {
        this.service = service;
        this.resultService = resultService;
        this.votingSessionMapper = votingSessionMapper;
        this.memberVoteMapper = memberVoteMapper;
        this.resultsTOMapper = resultsTOMapper;
    }

    @ApiOperation(value = "Abrir sessão", notes = "Operação para abrir uma sessão de votos para determinada pauta")
    @PostMapping(URL_PATTERN)
    public Mono<ResponseEntity<?>> openVotingSession(
            @ApiParam("Dados de entrada da abertura de sessão")
            @Valid @RequestBody final OpenVotingSessionTO openVotingSessionTO) {
        log.debug("Received data for open session for agenda {}", openVotingSessionTO.getAgendaId());
        return service.openVotingSession(votingSessionMapper.map(openVotingSessionTO))
                .map(votingAgenda -> ResponseEntity
                        .created(URI.create(URL_PATTERN + "/" + votingAgenda.getId()))
                        .build());
    }

    @ApiOperation(value = "Voto de um membro", notes = "Operação para um membro da cooperativa votar em uma sessão aberta de uma pauta da assembleia")
    @PatchMapping(URL_PATTERN + "/{session-id}/vote")
    public Mono<ResponseEntity<?>> computeMemberVote(
            @ApiParam(value = "Identificador da sessão aberta de uma pauta", required = true)
            @PathVariable("session-id") final String sessionId,
            @ApiParam("Entrada de dados do voto de um membro")
            @Valid @RequestBody final MemberVoteTO memberVoteTO) {
        log.debug("Received data for compute a vote for member {} and session {}", memberVoteTO.getMemberId(), sessionId);
        return service.computeMemberVote(sessionId, memberVoteMapper.map(memberVoteTO))
                .map(votingSession -> ResponseEntity.noContent().build());
    }

    @ApiOperation(value = "Fechamento do resultado da sessão", notes = "Operação para análise dos votos da sessão de uma pauta da assembleia")
    @PostMapping(URL_PATTERN + "/{session-id}/vote/result")
    public Mono<ResultsTO> computeVotes(
            @ApiParam(value = "Identificador da sessão aberta de uma pauta", required = true)
            @PathVariable("session-id") final String sessionId) {
        log.debug("Received call for compute votes for session {}", sessionId);
        return resultService.computeVotes(sessionId)
                .map(res -> resultsTOMapper.map(sessionId, res));
    }

    @ApiOperation(value = "Fechamento do resultado da sessão", notes = "Operação para análise dos votos da sessão de uma pauta da assembleia")
    @GetMapping(URL_PATTERN + "/{session-id}/vote/result")
    public Mono<ResultsTO> getResults(
            @ApiParam(value = "Identificador da sessão de uma pauta", required = true)
            @PathVariable("session-id") final String sessionId) {
        log.debug("Received call for get votes result for session {}", sessionId);
        return resultService.getResults(sessionId)
                .map(res -> resultsTOMapper.map(sessionId, res));
    }

}
