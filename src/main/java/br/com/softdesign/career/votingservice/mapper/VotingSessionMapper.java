package br.com.softdesign.career.votingservice.mapper;

import br.com.softdesign.career.votingservice.domain.MemberVote;
import br.com.softdesign.career.votingservice.domain.VotingSession;
import br.com.softdesign.career.votingservice.to.OpenVotingSessionTO;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VotingSessionMapper {

    static final Integer DEFAULT_DURATION = 1;

    public static VotingSession toModel(final OpenVotingSessionTO to) {
        Objects.requireNonNull(to, "Transfer Object cannot be null");
        final LocalDateTime now = LocalDateTime.now();
        return new VotingSession(UUID.randomUUID().toString(),
                to.getAgendaId(),
                now,
                now.plusMinutes(Optional.ofNullable(to.getDurationInMinutes()).orElse(DEFAULT_DURATION)));
    }

    public static VotingSession pushMemberVote(final VotingSession votingSession, final MemberVote memberVote) {
        return new VotingSession(
                votingSession.getId(),
                votingSession.getAgendaId(),
                votingSession.getStart(),
                votingSession.getEnd(),
                Optional.ofNullable(votingSession.getVotes())
                        .filter(votes -> !votes.isEmpty())
                        .map(votes -> Stream
                                .of(votes, Collections.singleton(memberVote))
                                .flatMap(Collection::stream)
                                .collect(Collectors.toSet()))
                        .orElseGet(() -> Collections.singleton(memberVote)));
    }
}
