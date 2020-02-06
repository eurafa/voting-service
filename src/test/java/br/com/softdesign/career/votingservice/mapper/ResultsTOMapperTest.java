package br.com.softdesign.career.votingservice.mapper;

import br.com.softdesign.career.votingservice.domain.VotingSessionResult;
import br.com.softdesign.career.votingservice.enums.Vote;
import br.com.softdesign.career.votingservice.to.ResultsTO;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class ResultsTOMapperTest {

    @Test
    void map() {
        // Given
        final String sessionId = "sessionId";
        final long totalVotes = 1L;
        final Map<String, Long> votes = Arrays.asList(Vote.values()).stream().collect(Collectors.toMap(Object::toString, v -> totalVotes));
        final VotingSessionResult votingSessionResult = new VotingSessionResult(sessionId, votes);

        // When
        final ResultsTO resultsTO = new ResultsTOMapper().map(sessionId, votingSessionResult);

        // Then
        assertThat(resultsTO).isNotNull();
        assertThat(resultsTO.getSessionId()).isEqualTo(sessionId);
        assertThat(resultsTO.getTotalVotesYes()).isEqualTo(totalVotes);
        assertThat(resultsTO.getTotalVotesNo()).isEqualTo(totalVotes);
    }

}