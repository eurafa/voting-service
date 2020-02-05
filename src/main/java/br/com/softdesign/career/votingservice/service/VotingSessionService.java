package br.com.softdesign.career.votingservice.service;

import br.com.softdesign.career.votingservice.domain.VotingAgenda;
import br.com.softdesign.career.votingservice.domain.VotingSession;
import br.com.softdesign.career.votingservice.exception.VotingAgendaNotFoundException;
import br.com.softdesign.career.votingservice.repository.VotingAgendaRepository;
import br.com.softdesign.career.votingservice.repository.VotingSessionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class VotingSessionService {

    private final VotingSessionRepository repository;

    private final VotingAgendaRepository agendaRepository;

    public VotingSessionService(final VotingSessionRepository repository, final VotingAgendaRepository agendaRepository) {
        this.repository = repository;
        this.agendaRepository = agendaRepository;
    }

    public Mono<VotingSession> openVotingSession(final VotingSession votingSession) {
        final Mono<VotingAgenda> votingAgendaMono = agendaRepository.findById(votingSession.getAgendaId());
        return votingAgendaMono
                .flatMap(votingAgenda -> this.repository.save(votingSession))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new VotingAgendaNotFoundException())));
    }

}
