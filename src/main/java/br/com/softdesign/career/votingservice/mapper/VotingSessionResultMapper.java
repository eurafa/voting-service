package br.com.softdesign.career.votingservice.mapper;

import br.com.softdesign.career.votingservice.domain.MemberVote;
import br.com.softdesign.career.votingservice.domain.VotingSession;
import br.com.softdesign.career.votingservice.domain.VotingSessionResult;

import java.util.Map;
import java.util.stream.Collectors;

public class VotingSessionResultMapper {

    public static VotingSessionResult toModel(final VotingSession votingSession) {
        final Map<String, Long> result = votingSession.getVotes().stream().collect(Collectors.groupingBy(MemberVote::getVote, Collectors.counting()));
        return new VotingSessionResult(votingSession.getId(), result);
    }

}
