package br.com.softdesign.career.votingservice.mapper;

import br.com.softdesign.career.votingservice.domain.MemberVote;
import br.com.softdesign.career.votingservice.domain.VotingSession;
import br.com.softdesign.career.votingservice.enums.Vote;
import br.com.softdesign.career.votingservice.to.OpenVotingSessionTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class VotingSessionMapperTest {

    @Test
    void testConstructor() {
        // Given
        final VotingSessionMapper mapper;

        // When
        mapper = new VotingSessionMapper();

        // Then
        assertThat(mapper).isNotNull();
    }

    @Test
    void toModel() {
        // Given
        final OpenVotingSessionTO openVotingSessionTO = new OpenVotingSessionTO("agendaId", 1);

        // When
        final VotingSession votingSession = VotingSessionMapper.toModel(openVotingSessionTO);

        // Then
        assertThat(votingSession).isNotNull();
        assertThat(votingSession.getId()).isNotNull();
        assertThat(votingSession.getAgendaId()).isEqualTo(openVotingSessionTO.getAgendaId());
        assertThat(votingSession.getStart()).isNotNull();
        assertThat(votingSession.getEnd()).isNotNull();
        assertThat(votingSession.getEnd()).isEqualTo(votingSession.getStart().plusMinutes(openVotingSessionTO.getDurationInMinutes()));
    }

    @Test
    void toModelDefaultDuration() {
        // Given
        final OpenVotingSessionTO openVotingSessionTO = new OpenVotingSessionTO("agendaId");

        // When
        final VotingSession votingSession = VotingSessionMapper.toModel(openVotingSessionTO);

        // Then
        assertThat(votingSession).isNotNull();
        assertThat(votingSession.getId()).isNotNull();
        assertThat(votingSession.getAgendaId()).isEqualTo(openVotingSessionTO.getAgendaId());
        assertThat(votingSession.getStart()).isNotNull();
        assertThat(votingSession.getEnd()).isNotNull();
        assertThat(votingSession.getEnd()).isEqualTo(votingSession.getStart().plusMinutes(VotingSessionMapper.DEFAULT_DURATION));
    }

    @Test
    void toModelFailure() {
        // Given

        // When
        final Throwable throwable = Assertions.catchThrowable(() -> VotingSessionMapper.toModel(null));

        // Then
        assertThat(throwable).isInstanceOf(NullPointerException.class);
    }

    @Test
    void pushMemberVote() {
        // Given
        final MemberVote memberVote = new MemberVote("memberId", Vote.YES.name(), LocalDateTime.now());
        final OpenVotingSessionTO openVotingSessionTO = new OpenVotingSessionTO("agendaId", 1);
        final VotingSession votingSession = VotingSessionMapper.toModel(openVotingSessionTO);

        // When
        final VotingSession votingSessionWithMemberVote = VotingSessionMapper.pushMemberVote(votingSession, memberVote);

        // Then
        assertThat(votingSessionWithMemberVote).isNotNull();
        assertThat(votingSessionWithMemberVote.getVotes()).isNotNull().hasSize(1);
        assertThat(votingSessionWithMemberVote.getVotes()).containsOnly(memberVote);
    }

    @Test
    void pushMemberVoteWithVotes() {
        // Given
        final Set<MemberVote> votes = Stream.of(
                new MemberVote("member1", Vote.YES.name(), LocalDateTime.now()),
                new MemberVote("member2", Vote.YES.name(), LocalDateTime.now()),
                new MemberVote("member3", Vote.NO.name(), LocalDateTime.now())
        ).collect(Collectors.toSet());
        final VotingSession votingSession = new VotingSession(UUID.randomUUID().toString(),
                "agendaId",
                LocalDateTime.now().minusMinutes(5),
                LocalDateTime.now().plusMinutes(5),
                votes);
        final MemberVote memberVote = new MemberVote("other", Vote.YES.name(), LocalDateTime.now());

        // When
        final VotingSession votingSessionWithMemberVote = VotingSessionMapper.pushMemberVote(votingSession, memberVote);

        // Then
        assertThat(votingSessionWithMemberVote).isNotNull();
        assertThat(votingSessionWithMemberVote.getVotes()).isNotNull().hasSize(votes.size() + 1);
        assertThat(votingSessionWithMemberVote.getVotes()).containsAnyElementsOf(votes);
        assertThat(votingSessionWithMemberVote.getVotes()).contains(memberVote);
    }

}