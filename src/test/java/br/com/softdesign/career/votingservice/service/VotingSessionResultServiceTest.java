package br.com.softdesign.career.votingservice.service;

import br.com.softdesign.career.votingservice.domain.MemberVote;
import br.com.softdesign.career.votingservice.domain.VotingSession;
import br.com.softdesign.career.votingservice.domain.VotingSessionResult;
import br.com.softdesign.career.votingservice.enums.Vote;
import br.com.softdesign.career.votingservice.exception.UnfinishedVotingSessionException;
import br.com.softdesign.career.votingservice.exception.VotingSessionNotFoundException;
import br.com.softdesign.career.votingservice.exception.VotingSessionResultsNotFoundException;
import br.com.softdesign.career.votingservice.mapper.VotingSessionResultMapper;
import br.com.softdesign.career.votingservice.repository.VotingSessionRepository;
import br.com.softdesign.career.votingservice.repository.VotingSessionResultRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

public class VotingSessionResultServiceTest {

    private final VotingSessionResultRepository repository = Mockito.mock(VotingSessionResultRepository.class);

    private final VotingSessionRepository votingSessionRepository = Mockito.mock(VotingSessionRepository.class);

    private final VotingSessionResultService service = new VotingSessionResultService(repository, votingSessionRepository);

    @Test
    void computeVotes() {
        // Given
        final String sessionId = "sessionId";
        final String agendaId = "agendaId";
        final LocalDateTime start = LocalDateTime.now().minusMinutes(1);
        final LocalDateTime end = LocalDateTime.now();
        final Set<MemberVote> votes = Stream.of(
                new MemberVote("member1", Vote.YES.name(), LocalDateTime.now()),
                new MemberVote("member2", Vote.YES.name(), LocalDateTime.now()),
                new MemberVote("member3", Vote.NO.name(), LocalDateTime.now())
        ).collect(Collectors.toSet());
        final VotingSession votingSession = new VotingSession(sessionId, agendaId, start, end, votes);
        final VotingSessionResult votingSessionResult = VotingSessionResultMapper.toModel(votingSession);
        given(votingSessionRepository.findById(sessionId)).willReturn(Mono.just(votingSession));
        given(repository.save(any())).willReturn(Mono.just(votingSessionResult));

        // When
        final Mono<VotingSessionResult> votingSessionResultMono = service.computeVotes(sessionId);

        // Then
        StepVerifier.create(votingSessionResultMono)
                .expectNext(votingSessionResult)
                .verifyComplete();
    }

    @Test
    void computeVotesEmpty() {
        // Given
        final String sessionId = "sessionId";
        final String agendaId = "agendaId";
        final LocalDateTime start = LocalDateTime.now().minusMinutes(1);
        final LocalDateTime end = LocalDateTime.now();
        final VotingSession votingSession = new VotingSession(sessionId, agendaId, start, end);
        final VotingSessionResult votingSessionResult = VotingSessionResultMapper.toModel(votingSession);
        given(votingSessionRepository.findById(sessionId)).willReturn(Mono.just(votingSession));
        given(repository.save(any())).willReturn(Mono.just(votingSessionResult));

        // When
        final Mono<VotingSessionResult> votingSessionResultMono = service.computeVotes(sessionId);

        // Then
        StepVerifier.create(votingSessionResultMono)
                .expectNext(votingSessionResult)
                .verifyComplete();
    }

    @Test
    void computeVotesFailureSessionNotFound() {
        // Given
        final String sessionId = "sessionId";
        given(votingSessionRepository.findById(anyString())).willReturn(Mono.error(() -> new VotingSessionNotFoundException(sessionId)));

        // When
        final Mono<VotingSessionResult> votingSessionMono = service.computeVotes(sessionId);

        // Then
        StepVerifier.create(votingSessionMono)
                .expectError(VotingSessionNotFoundException.class)
                .verify();
    }

    @Test
    void computeVotesFailureSessionInProgress() {
        // Given
        final String sessionId = UUID.randomUUID().toString();
        final LocalDateTime start = LocalDateTime.now().minusMinutes(1);
        final LocalDateTime end = LocalDateTime.now().plusHours(1);
        final VotingSession votingSession = new VotingSession(sessionId, "agendaId", start, end);
        given(votingSessionRepository.findById(anyString())).willReturn(Mono.just(votingSession));

        // When
        final Mono<VotingSessionResult> votingSessionMono = service.computeVotes(sessionId);

        // Then
        StepVerifier.create(votingSessionMono)
                .expectError(UnfinishedVotingSessionException.class)
                .verify();
    }

    @Test
    void getResults() {
        // Given
        final String sessionId = "sessionId";
        final VotingSessionResult votingSessionResult = new VotingSessionResult(sessionId, Collections.emptyMap());
        given(repository.findById(sessionId)).willReturn(Mono.just(votingSessionResult));

        // When
        final Mono<VotingSessionResult> votingSessionResultMono = service.getResults(sessionId);

        // Then
        StepVerifier.create(votingSessionResultMono)
                .expectNext(votingSessionResult)
                .verifyComplete();
    }

    @Test
    void getResultsFailureNotFound() {
        // Given
        final String sessionId = "sessionId";
        given(repository.findById(sessionId)).willReturn(Mono.error(() -> new VotingSessionResultsNotFoundException(sessionId)));

        // When
        final Mono<VotingSessionResult> votingSessionResultMono = service.getResults(sessionId);

        // Then
        StepVerifier.create(votingSessionResultMono)
                .expectError(VotingSessionResultsNotFoundException.class)
                .verify();
    }

}