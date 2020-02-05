package br.com.softdesign.career.votingservice.repository;

import br.com.softdesign.career.votingservice.domain.VotingSessionResult;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotingSessionResultRepository extends ReactiveMongoRepository<VotingSessionResult, String> {

}
