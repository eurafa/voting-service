package br.com.softdesign.career.votingservice.controller;

import br.com.softdesign.career.votingservice.domain.VotingSessionResult;
import br.com.softdesign.career.votingservice.mapper.MemberVoteMapper;
import br.com.softdesign.career.votingservice.mapper.VotingSessionMapper;
import br.com.softdesign.career.votingservice.service.VotingSessionResultService;
import br.com.softdesign.career.votingservice.service.VotingSessionService;
import br.com.softdesign.career.votingservice.to.MemberVoteTO;
import br.com.softdesign.career.votingservice.to.OpenVotingSessionTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class VotingSessionController {

    public static final String URL_PATTERN = "/voting-session";

    private final VotingSessionService service;

    private final VotingSessionResultService resultService;

    public VotingSessionController(final VotingSessionService service, final VotingSessionResultService resultService) {
        this.service = service;
        this.resultService = resultService;
    }

    @ApiOperation(value = "Abrir sessão", notes = "Operação para abrir uma sessão de votos para determinada pauta")
    @PostMapping(URL_PATTERN)
    public Mono<ResponseEntity<?>> openVotingSession(
            @ApiParam("Dados de entrada da abertura de sessão")
            @Valid @RequestBody final OpenVotingSessionTO openVotingSessionTO) {
        return service.openVotingSession(VotingSessionMapper.toModel(openVotingSessionTO))
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
        return service.computeMemberVote(sessionId, MemberVoteMapper.toModel(memberVoteTO))
                .map(votingSession -> ResponseEntity.noContent().build());
    }

    @ApiOperation(value = "Fechamento do resultado da sessão", notes = "Operação para análise dos votos da sessão de uma pauta da assembleia")
    @PostMapping(URL_PATTERN + "/{session-id}/vote/result")
    public Mono<VotingSessionResult> computeVotes(
            @ApiParam(value = "Identificador da sessão aberta de uma pauta", required = true)
            @PathVariable("session-id") final String sessionId) {
        return resultService.computeVotes(sessionId);
    }

}
