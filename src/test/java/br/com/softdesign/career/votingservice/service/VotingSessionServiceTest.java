package br.com.softdesign.career.votingservice.service;

import br.com.softdesign.career.votingservice.component.CpfValidatorComponent;
import br.com.softdesign.career.votingservice.domain.MemberVote;
import br.com.softdesign.career.votingservice.domain.VotingAgenda;
import br.com.softdesign.career.votingservice.domain.VotingSession;
import br.com.softdesign.career.votingservice.enums.MemberCpfValidationStatus;
import br.com.softdesign.career.votingservice.enums.Vote;
import br.com.softdesign.career.votingservice.exception.*;
import br.com.softdesign.career.votingservice.mapper.VotingSessionMapper;
import br.com.softdesign.career.votingservice.repository.VotingAgendaRepository;
import br.com.softdesign.career.votingservice.repository.VotingSessionRepository;
import br.com.softdesign.career.votingservice.to.OpenVotingSessionTO;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

public class VotingSessionServiceTest {

    private final VotingSessionRepository repository = Mockito.mock(VotingSessionRepository.class);

    private final VotingAgendaRepository agendaRepository = Mockito.mock(VotingAgendaRepository.class);

    private final CpfValidatorComponent cpfValidatorComponent = Mockito.mock(CpfValidatorComponent.class);

    private final VotingSessionService service = new VotingSessionService(repository, agendaRepository, cpfValidatorComponent);

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
        given(cpfValidatorComponent.validateCpf(anyString())).willReturn(MemberCpfValidationStatus.ABLE_TO_VOTE);
        given(repository.save(any())).willReturn(Mono.just(votingSession));

        // When
        final Mono<VotingSession> votingSessionMono = service.computeMemberVote(sessionId, memberVote);

        // Then
        StepVerifier.create(votingSessionMono)
                .expectNext(votingSession)
                .verifyComplete();
    }

    @Test
    void computeMemberVoteFailureUnableToVote() {
        // Given
        final String sessionId = UUID.randomUUID().toString();
        final LocalDateTime start = LocalDateTime.now();
        final LocalDateTime end = LocalDateTime.now().plusMinutes(1);
        final VotingSession votingSession = new VotingSession(sessionId, "agendaId", start, end);
        final MemberVote memberVote = new MemberVote("memberId", Vote.YES.name(), LocalDateTime.now());
        given(repository.findById(anyString())).willReturn(Mono.just(votingSession));
        given(cpfValidatorComponent.validateCpf(anyString())).willReturn(MemberCpfValidationStatus.UNABLE_TO_VOTE);
        given(repository.save(any())).willReturn(Mono.just(votingSession));

        // When
        final Mono<VotingSession> votingSessionMono = service.computeMemberVote(sessionId, memberVote);

        // Then
        StepVerifier.create(votingSessionMono)
                .expectError(MemberUnableToVoteException.class)
                .verify();
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
        given(cpfValidatorComponent.validateCpf(anyString())).willReturn(MemberCpfValidationStatus.ABLE_TO_VOTE);

        // When
        final Mono<VotingSession> votingSessionMono = service.computeMemberVote(sessionId, memberVote);

        // Then
        StepVerifier.create(votingSessionMono)
                .expectError(MemberVoteAlreadyComputedException.class)
                .verify();
    }

    @Test
    void pushMemberVote() {
        // Given
        final MemberVote memberVote = new MemberVote("memberId", Vote.YES.name(), LocalDateTime.now());
        final OpenVotingSessionTO openVotingSessionTO = new OpenVotingSessionTO("agendaId", 1);
        final VotingSession votingSession = new VotingSessionMapper().map(openVotingSessionTO);

        // When
        final VotingSession votingSessionWithMemberVote = service.pushMemberVote(votingSession, memberVote);

        // Then
        assertThat(votingSessionWithMemberVote).isNotNull();
        assertThat(votingSessionWithMemberVote.getVotes()).isNotNull().hasSize(1);
        assertThat(votingSessionWithMemberVote.getVotes()).containsOnly(memberVote);
    }

    @Test
    void pushMemberVoteWithVotes() {
        // Given
        final Set<MemberVote> votes = Stream.of(
                new MemberVote("member1", Vote.YES.name(), LocalDateTime.now()),
                new MemberVote("member2", Vote.YES.name(), LocalDateTime.now()),
                new MemberVote("member3", Vote.NO.name(), LocalDateTime.now())
        ).collect(Collectors.toSet());
        final VotingSession votingSession = new VotingSession(UUID.randomUUID().toString(),
                "agendaId",
                LocalDateTime.now().minusMinutes(5),
                LocalDateTime.now().plusMinutes(5),
                votes);
        final MemberVote memberVote = new MemberVote("other", Vote.YES.name(), LocalDateTime.now());

        // When
        final VotingSession votingSessionWithMemberVote = service.pushMemberVote(votingSession, memberVote);

        // Then
        assertThat(votingSessionWithMemberVote).isNotNull();
        assertThat(votingSessionWithMemberVote.getVotes()).isNotNull().hasSize(votes.size() + 1);
        assertThat(votingSessionWithMemberVote.getVotes()).containsAnyElementsOf(votes);
        assertThat(votingSessionWithMemberVote.getVotes()).contains(memberVote);
    }

}