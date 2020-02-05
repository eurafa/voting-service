package br.com.softdesign.career.votingservice.service;

import br.com.softdesign.career.votingservice.domain.VotingAgenda;
import br.com.softdesign.career.votingservice.repository.VotingAgendaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class VotingAgendaService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final VotingAgendaRepository repository;

    public VotingAgendaService(final VotingAgendaRepository repository) {
        this.repository = repository;
    }

    public Mono<VotingAgenda> createVotingAgenda(final VotingAgenda votingAgenda) {
        log.info("Saving agenda with title {} identified by {}", votingAgenda.getTitle(), votingAgenda.getId());
        final Mono<VotingAgenda> votingAgendaMono = this.repository.save(votingAgenda);
        log.info("Agenda {} has been saved successfully", votingAgenda.getId());
        return votingAgendaMono;
    }

}
