package br.com.softdesign.career.votingservice.controller;

import br.com.softdesign.career.votingservice.domain.VotingSessionResult;
import br.com.softdesign.career.votingservice.mapper.MemberVoteMapper;
import br.com.softdesign.career.votingservice.mapper.VotingSessionMapper;
import br.com.softdesign.career.votingservice.service.VotingSessionResultService;
import br.com.softdesign.career.votingservice.service.VotingSessionService;
import br.com.softdesign.career.votingservice.to.MemberVoteTO;
import br.com.softdesign.career.votingservice.to.OpenVotingSessionTO;
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

    @PostMapping(URL_PATTERN)
    public Mono<ResponseEntity<?>> openVotingSession(@Valid @RequestBody final OpenVotingSessionTO openVotingSessionTO) {
        return service.openVotingSession(VotingSessionMapper.toModel(openVotingSessionTO))
                .map(votingAgenda -> ResponseEntity
                        .created(URI.create(URL_PATTERN + "/" + votingAgenda.getId()))
                        .build());
    }

    @PatchMapping(URL_PATTERN + "/{session-id}/vote")
    public Mono<ResponseEntity<?>> computeMemberVote(@PathVariable("session-id") final String sessionId, @Valid @RequestBody final MemberVoteTO memberVoteTO) {
        return service.computeMemberVote(sessionId, MemberVoteMapper.toModel(memberVoteTO))
                .map(votingSession -> ResponseEntity.noContent().build());
    }

    @PostMapping(URL_PATTERN + "/{session-id}/vote/result")
    public Mono<VotingSessionResult> computeVotes(@PathVariable("session-id") final String sessionId) {
        return resultService.computeVotes(sessionId);
    }

}
