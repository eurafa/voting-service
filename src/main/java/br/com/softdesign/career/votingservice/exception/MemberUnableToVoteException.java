package br.com.softdesign.career.votingservice.exception;

public class MemberUnableToVoteException extends RuntimeException {

    public MemberUnableToVoteException(final String memberId) {
        super(String.format("Member %s is unable to vote", memberId));
    }

}
