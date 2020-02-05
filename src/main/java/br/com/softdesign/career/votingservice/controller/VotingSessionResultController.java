package br.com.softdesign.career.votingservice.controller;

import br.com.softdesign.career.votingservice.domain.VotingSessionResult;
import br.com.softdesign.career.votingservice.service.VotingSessionResultService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = VotingSessionResultController.URL_PATTERN)
public class VotingSessionResultController {

    public static final String URL_PATTERN = "/voting-session-result";

    private final VotingSessionResultService service;

    public VotingSessionResultController(final VotingSessionResultService service) {
        this.service = service;
    }

    @PostMapping("/{session-id}")
    public Mono<VotingSessionResult> computeVotes(@PathVariable("session-id") final String sessionId) {
        return service.computeVotes(sessionId);
    }

}
