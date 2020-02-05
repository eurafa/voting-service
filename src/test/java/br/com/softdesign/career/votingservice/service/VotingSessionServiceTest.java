package br.com.softdesign.career.votingservice.service;

import br.com.softdesign.career.votingservice.domain.VotingAgenda;
import br.com.softdesign.career.votingservice.domain.VotingSession;
import br.com.softdesign.career.votingservice.exception.VotingAgendaNotFoundException;
import br.com.softdesign.career.votingservice.repository.VotingAgendaRepository;
import br.com.softdesign.career.votingservice.repository.VotingSessionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;

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

}