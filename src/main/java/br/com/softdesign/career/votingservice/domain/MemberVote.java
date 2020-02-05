package br.com.softdesign.career.votingservice.domain;

import java.time.LocalDateTime;

public class MemberVote {

    private final String memberId;

    private final String vote;

    private final LocalDateTime when;

    public MemberVote(final String memberId, final String vote, final LocalDateTime when) {
        this.memberId = memberId;
        this.vote = vote;
        this.when = when;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getVote() {
        return vote;
    }

    public LocalDateTime getWhen() {
        return when;
    }

}
