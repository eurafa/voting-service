package br.com.softdesign.career.votingservice.mapper;

import br.com.softdesign.career.votingservice.domain.VotingSessionResult;
import br.com.softdesign.career.votingservice.enums.Vote;
import br.com.softdesign.career.votingservice.to.ResultsTO;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ResultsTOMapper {

    public ResultsTO map(final String sessionId, final VotingSessionResult votingSessionResult) {
        final Map<String, Long> result = votingSessionResult.getResult();
        return new ResultsTO(sessionId, result.get(Vote.YES.name()), result.get(Vote.NO.name()));
    }

}
