package br.com.softdesign.career.votingservice.repository;

import br.com.softdesign.career.votingservice.domain.VotingSession;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotingSessionRepository extends ReactiveMongoRepository<VotingSession, String> {

}
