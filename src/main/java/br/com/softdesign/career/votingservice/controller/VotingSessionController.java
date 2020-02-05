package br.com.softdesign.career.votingservice.controller;

import br.com.softdesign.career.votingservice.mapper.MemberVoteMapper;
import br.com.softdesign.career.votingservice.mapper.VotingSessionMapper;
import br.com.softdesign.career.votingservice.service.VotingSessionService;
import br.com.softdesign.career.votingservice.to.MemberVoteTO;
import br.com.softdesign.career.votingservice.to.OpenVotingSessionTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = VotingSessionController.URL_PATTERN)
public class VotingSessionController {

    public static final String URL_PATTERN = "/voting-session";

    private final VotingSessionService service;

    public VotingSessionController(final VotingSessionService service) {
        this.service = service;
    }

    @PostMapping
    public Mono<ResponseEntity<?>> openVotingSession(@Valid @RequestBody final OpenVotingSessionTO openVotingSessionTO) {
        return service.openVotingSession(VotingSessionMapper.toModel(openVotingSessionTO))
                .map(votingAgenda -> ResponseEntity
                        .created(URI.create(URL_PATTERN + "/" + votingAgenda.getId()))
                        .build());
    }

    @PatchMapping
    public Mono<ResponseEntity<?>> computeMemberVote(@Valid @RequestBody final MemberVoteTO memberVoteTO) {
        return service.computeMemberVote(memberVoteTO.getSessionId(), MemberVoteMapper.toModel(memberVoteTO))
                .map(votingSession -> ResponseEntity.noContent().build());
    }

}
