package br.com.softdesign.career.votingservice.service;

import br.com.softdesign.career.votingservice.component.VotingSessionResultPublisherComponent;
import br.com.softdesign.career.votingservice.domain.VotingSession;
import br.com.softdesign.career.votingservice.domain.VotingSessionResult;
import br.com.softdesign.career.votingservice.exception.UnfinishedVotingSessionException;
import br.com.softdesign.career.votingservice.exception.VotingSessionNotFoundException;
import br.com.softdesign.career.votingservice.exception.VotingSessionResultsNotFoundException;
import br.com.softdesign.career.votingservice.mapper.VotingSessionResultMapper;
import br.com.softdesign.career.votingservice.repository.VotingSessionRepository;
import br.com.softdesign.career.votingservice.repository.VotingSessionResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class VotingSessionResultService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final VotingSessionResultRepository repository;

    private final VotingSessionRepository votingSessionRepository;

    private final VotingSessionResultMapper votingSessionResultMapper;

    private final VotingSessionResultPublisherComponent resultPublisherComponent;

    public VotingSessionResultService(final VotingSessionResultRepository repository,
                                      final VotingSessionRepository votingSessionRepository,
                                      final VotingSessionResultMapper votingSessionResultMapper,
                                      final VotingSessionResultPublisherComponent resultPublisherComponent) {
        this.repository = repository;
        this.votingSessionRepository = votingSessionRepository;
        this.votingSessionResultMapper = votingSessionResultMapper;
        this.resultPublisherComponent = resultPublisherComponent;
    }

    public Mono<VotingSessionResult> getResults(final String votingSessionId) {
        log.info("Getting results for session {}", votingSessionId);
        return this.repository.findById(votingSessionId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(() -> new VotingSessionResultsNotFoundException(votingSessionId))));
    }

    public Mono<VotingSessionResult> computeVotes(final String votingSessionId) {
        log.info("Computing votes for session {}", votingSessionId);
        final Mono<VotingSession> votingSessionMono = this.votingSessionRepository.findById(votingSessionId);
        return votingSessionMono
                .flatMap(this::validateSession)
                .flatMap(votingSession -> this.repository.save(votingSessionResultMapper.map(votingSession)))
                .doOnNext(resultPublisherComponent::publish)
                .switchIfEmpty(Mono.defer(() -> Mono.error(() -> new VotingSessionNotFoundException(votingSessionId))));
    }

    private Mono<VotingSession> validateSession(final VotingSession votingSession) {
        return Mono.just(votingSession)
                .filter(vs -> LocalDateTime.now().isAfter(vs.getEnd()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(() -> new UnfinishedVotingSessionException(votingSession.getId(), votingSession.getEnd()))));
    }

}
