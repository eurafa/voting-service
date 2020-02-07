package br.com.softdesign.career.votingservice.mapper;

import br.com.softdesign.career.votingservice.domain.MemberVote;
import br.com.softdesign.career.votingservice.enums.Vote;
import br.com.softdesign.career.votingservice.to.MemberVoteTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberVoteMapperTest {

    private final MemberVoteMapper memberVoteMapper = new MemberVoteMapper();

    @Test
    void map() {
        // Given
        final MemberVoteTO memberVoteTO = new MemberVoteTO("memberId", Vote.YES);

        // When
        final MemberVote memberVote = memberVoteMapper.map(memberVoteTO);

        // Then
        assertThat(memberVote).isNotNull();
        assertThat(memberVote.getMemberId()).isEqualTo(memberVoteTO.getMemberId());
        assertThat(memberVote.getVote()).isEqualTo(memberVoteTO.getVote().name());
        assertThat(memberVote.getWhen()).isNotNull();
    }

    @Test
    void mapFailure() {
        // Given

        // When
        final Throwable throwable = Assertions.catchThrowable(() -> memberVoteMapper.map(null));

        // Then
        assertThat(throwable).isInstanceOf(NullPointerException.class);
    }

}