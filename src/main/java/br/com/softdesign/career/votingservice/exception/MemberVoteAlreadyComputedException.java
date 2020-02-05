package br.com.softdesign.career.votingservice.exception;

public class MemberVoteAlreadyComputedException extends RuntimeException {

    public MemberVoteAlreadyComputedException(final String memberId, final String sessionId) {
        super(String.format("Member %s already voted in session %s", memberId, sessionId));
    }

}
