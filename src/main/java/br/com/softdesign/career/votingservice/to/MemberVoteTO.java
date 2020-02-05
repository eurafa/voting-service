package br.com.softdesign.career.votingservice.to;

import br.com.softdesign.career.votingservice.enums.Vote;

public class MemberVoteTO {

    private String memberId;

    private Vote vote;

    public MemberVoteTO(final String memberId, final Vote vote) {
        this.memberId = memberId;
        this.vote = vote;
    }

    public String getMemberId() {
        return memberId;
    }

    public Vote getVote() {
        return vote;
    }

}
