package br.com.softdesign.career.votingservice.service;

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
import reactor.core.publisher.SynchronousSink;

import java.time.LocalDateTime;

@Service
public class VotingSessionResultService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final VotingSessionResultRepository repository;

    private final VotingSessionRepository votingSessionRepository;

    public VotingSessionResultService(final VotingSessionResultRepository repository, final VotingSessionRepository votingSessionRepository) {
        this.repository = repository;
        this.votingSessionRepository = votingSessionRepository;
    }

    public Mono<VotingSessionResult> getResults(final String votingSessionId) {
        log.info("Getting results for session {}", votingSessionId);
        return this.repository.findById(votingSessionId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(VotingSessionResultsNotFoundException::new)));
    }

    public Mono<VotingSessionResult> computeVotes(final String votingSessionId) {
        log.info("Computing votes for session {}", votingSessionId);
        final Mono<VotingSession> votingSessionMono = this.votingSessionRepository.findById(votingSessionId);
        return votingSessionMono
                .handle(this::computeVotesHandler)
                .flatMap(votingSession -> this.repository.save(VotingSessionResultMapper.toModel(votingSession)))
                .switchIfEmpty(Mono.defer(() -> Mono.error(VotingSessionNotFoundException::new)));
    }

    private void computeVotesHandler(final VotingSession votingSession, final SynchronousSink<VotingSession> sink) {
        final boolean isOpened = LocalDateTime.now().isBefore(votingSession.getEnd());
        if (isOpened) {
            sink.error(new UnfinishedVotingSessionException());
        } else {
            sink.next(votingSession);
        }
    }

}
