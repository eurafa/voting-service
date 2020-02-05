package br.com.softdesign.career.votingservice.mapper;

import br.com.softdesign.career.votingservice.domain.MemberVote;
import br.com.softdesign.career.votingservice.domain.VotingSession;
import br.com.softdesign.career.votingservice.domain.VotingSessionResult;
import br.com.softdesign.career.votingservice.enums.Vote;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class VotingSessionResultMapperTest {

    @Test
    void testConstructor() {
        // Given
        final VotingSessionResultMapper mapper;

        // When
        mapper = new VotingSessionResultMapper();

        // Then
        assertThat(mapper).isNotNull();
    }

    @Test
    void map() {
        // Given
        final LocalDateTime now = LocalDateTime.now();
        final int yesTotalVotes = 10;
        final int noTotalVotes = 3;
        final Set<MemberVote> yesVotes = IntStream.rangeClosed(1, yesTotalVotes)
                .mapToObj(i -> new MemberVote("member-yes-" + i, Vote.YES.name(), now))
                .collect(Collectors.toSet());
        final Set<MemberVote> noVotes = IntStream.rangeClosed(1, noTotalVotes)
                .mapToObj(i -> new MemberVote("member-no-" + i, Vote.NO.name(), now))
                .collect(Collectors.toSet());
        final Set<MemberVote> votes = Stream.of(yesVotes, noVotes).flatMap(Collection::stream).collect(Collectors.toSet());
        final VotingSession votingSession = new VotingSession("sessionId", "agendaId", now, now.plusMinutes(1), votes);

        // When
        final VotingSessionResult votingSessionResult = VotingSessionResultMapper.map(votingSession);

        // Then
        assertThat(votingSessionResult).isNotNull();
        assertThat(votingSessionResult.getId()).isEqualTo(votingSession.getId());
        assertThat(votingSessionResult.getResult().get(Vote.YES.name())).isEqualTo(yesTotalVotes);
        assertThat(votingSessionResult.getResult().get(Vote.NO.name())).isEqualTo(noTotalVotes);
    }

}