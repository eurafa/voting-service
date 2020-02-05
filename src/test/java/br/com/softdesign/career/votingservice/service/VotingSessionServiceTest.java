package br.com.softdesign.career.votingservice.service;

import br.com.softdesign.career.votingservice.domain.MemberVote;
import br.com.softdesign.career.votingservice.domain.VotingAgenda;
import br.com.softdesign.career.votingservice.domain.VotingSession;
import br.com.softdesign.career.votingservice.enums.Vote;
import br.com.softdesign.career.votingservice.exception.MemberVoteAlreadyComputedException;
import br.com.softdesign.career.votingservice.exception.VotingAgendaNotFoundException;
import br.com.softdesign.career.votingservice.exception.VotingSessionClosedException;
import br.com.softdesign.career.votingservice.exception.VotingSessionNotFoundException;
import br.com.softdesign.career.votingservice.repository.VotingAgendaRepository;
import br.com.softdesign.career.votingservice.repository.VotingSessionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

public class VotingSessionServiceTest {

    private final VotingSessionRepository repository = Mockito.mock(VotingSessionRepository.class);

    private final VotingAgendaRepository agendaRepository = Mockito.mock(VotingAgendaRepository.class);

    private final VotingSessionService service = new VotingSessionService(repository, agendaRepository);

    @Test
    void openVotingSession() {
        // Given
        final String agendaId = "agendaId";
        final VotingAgenda votingAgenda = new VotingAgenda(agendaId, "Title", "Description", LocalDateTime.now());
        final String id = UUID.randomUUID().toString();
        final LocalDateTime start = LocalDateTime.now();
        final LocalDateTime end = LocalDateTime.now().plusMinutes(1);
        final VotingSession votingSession = new VotingSession(id, agendaId, start, end);
        given(agendaRepository.findById(agendaId)).willReturn(Mono.just(votingAgenda));
        given(repository.save(votingSession)).willReturn(Mono.just(votingSession));

        // When
        final Mono<VotingSession> votingSessionMono = service.openVotingSession(votingSession);

        // Then
        StepVerifier.create(votingSessionMono)
                .expectNext(votingSession)
                .verifyComplete();
    }

    @Test
    void openVotingSessionFailureAgendaNotFound() {
        // Given
        final String id = UUID.randomUUID().toString();
        final String agendaId = "agendaId";
        final LocalDateTime start = LocalDateTime.now();
        final LocalDateTime end = LocalDateTime.now().plusMinutes(1);
        final VotingSession votingSession = new VotingSession(id, agendaId, start, end);
        given(agendaRepository.findById(agendaId)).willReturn(Mono.empty());
        given(repository.save(votingSession)).willReturn(Mono.just(votingSession));

        // When
        final Mono<VotingSession> votingSessionMono = service.openVotingSession(votingSession);

        // Then
        StepVerifier.create(votingSessionMono)
                .expectError(VotingAgendaNotFoundException.class)
                .verify();
    }

    @Test
    void computeMemberVote() {
        // Given
        final String sessionId = UUID.randomUUID().toString();
        final LocalDateTime start = LocalDateTime.now();
        final LocalDateTime end = LocalDateTime.now().plusMinutes(1);
        final VotingSession votingSession = new VotingSession(sessionId, "agendaId", start, end);
        final MemberVote memberVote = new MemberVote("memberId", Vote.YES.name(), LocalDateTime.now());
        given(repository.findById(anyString())).willReturn(Mono.just(votingSession));
        given(repository.save(any())).willReturn(Mono.just(votingSession));

        // When
        final Mono<VotingSession> votingSessionMono = service.computeMemberVote(sessionId, memberVote);

        // Then
        StepVerifier.create(votingSessionMono)
                .expectNext(votingSession)
                .verifyComplete();
    }

    @Test
    void computeMemberVoteFailureSessionNotFound() {
        // Given
        final String sessionId = "sessionId";
        final MemberVote memberVote = new MemberVote("memberId", Vote.YES.name(), LocalDateTime.now());
        given(repository.findById(anyString())).willReturn(Mono.error(() -> new VotingSessionNotFoundException(sessionId)));

        // When
        final Mono<VotingSession> votingSessionMono = service.computeMemberVote(sessionId, memberVote);

        // Then
        StepVerifier.create(votingSessionMono)
                .expectError(VotingSessionNotFoundException.class)
                .verify();
    }

    @Test
    void computeMemberVoteFailureSessionClosed() {
        // Given
        final String sessionId = UUID.randomUUID().toString();
        final LocalDateTime start = LocalDateTime.now().minusMinutes(1);
        final LocalDateTime end = LocalDateTime.now();
        final VotingSession votingSession = new VotingSession(sessionId, "agendaId", start, end);
        final MemberVote memberVote = new MemberVote("memberId", Vote.YES.name(), LocalDateTime.now());
        given(repository.findById(anyString())).willReturn(Mono.just(votingSession));

        // When
        final Mono<VotingSession> votingSessionMono = service.computeMemberVote(sessionId, memberVote);

        // Then
        StepVerifier.create(votingSessionMono)
                .expectError(VotingSessionClosedException.class)
                .verify();
    }

    @Test
    void computeMemberVoteFailureAlreadyVote() {
        // Given
        final String sessionId = UUID.randomUUID().toString();
        final LocalDateTime start = LocalDateTime.now();
        final LocalDateTime end = LocalDateTime.now().plusMinutes(1);
        final MemberVote memberVote = new MemberVote("memberId", Vote.YES.name(), LocalDateTime.now());
        final VotingSession votingSession = new VotingSession(sessionId, "agendaId", start, end, Collections.singleton(memberVote));
        given(repository.findById(anyString())).willReturn(Mono.just(votingSession));

        // When
        final Mono<VotingSession> votingSessionMono = service.computeMemberVote(sessionId, memberVote);

        // Then
        StepVerifier.create(votingSessionMono)
                .expectError(MemberVoteAlreadyComputedException.class)
                .verify();
    }

}