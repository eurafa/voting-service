package br.com.softdesign.career.votingservice.repository;

import br.com.softdesign.career.votingservice.domain.VotingAgenda;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotingAgendaRepository extends ReactiveMongoRepository<VotingAgenda, String> {

}
