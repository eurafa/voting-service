package br.com.softdesign.career.votingservice.mapper;

import br.com.softdesign.career.votingservice.domain.MemberVote;
import br.com.softdesign.career.votingservice.to.MemberVoteTO;

import java.time.LocalDateTime;
import java.util.Objects;

public class MemberVoteMapper {

    public static MemberVote toModel(final MemberVoteTO to) {
        Objects.requireNonNull(to, "Transfer Object cannot be null");
        return new MemberVote(
                to.getMemberId(),
                to.getVote().name(),
                LocalDateTime.now());
    }

}
