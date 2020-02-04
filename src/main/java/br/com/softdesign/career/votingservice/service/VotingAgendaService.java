package br.com.softdesign.career.votingservice.service;

import br.com.softdesign.career.votingservice.domain.VotingAgenda;
import br.com.softdesign.career.votingservice.repository.VotingAgendaRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class VotingAgendaService {

    private final VotingAgendaRepository repository;

    private final VotingAgendaRepository reactiveRepository;

    public VotingAgendaService(final VotingAgendaRepository repository, final VotingAgendaRepository reactiveRepository) {
        this.repository = repository;
        this.reactiveRepository = reactiveRepository;
    }

    public Mono<VotingAgenda> createVotingAgenda(final VotingAgenda votingAgenda) {
        return this.reactiveRepository.save(votingAgenda);
    }

}
