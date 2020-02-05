package br.com.softdesign.career.votingservice.service;

import br.com.softdesign.career.votingservice.domain.VotingAgenda;
import br.com.softdesign.career.votingservice.repository.VotingAgendaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.BDDMockito.given;

public class VotingAgendaServiceTest {

    private final VotingAgendaRepository repository = Mockito.mock(VotingAgendaRepository.class);

    private final VotingAgendaService service = new VotingAgendaService(repository);

    @Test
    void createVotingAgenda() {
        // Given
        final String id = UUID.randomUUID().toString();
        final String title = "Title";
        final String description = "Description";
        final LocalDateTime createdDate = LocalDateTime.now();
        final VotingAgenda votingAgenda = new VotingAgenda(id, title, description, createdDate);
        given(repository.save(votingAgenda)).willReturn(Mono.just(votingAgenda));

        // When
        final Mono<VotingAgenda> votingAgendaMono = service.createVotingAgenda(votingAgenda);

        // Then
        StepVerifier.create(votingAgendaMono)
                .expectNext(votingAgenda)
                .verifyComplete();
    }

}