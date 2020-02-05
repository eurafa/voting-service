package br.com.softdesign.career.votingservice.exception;

public class MemberVoteAlreadyComputedException extends RuntimeException {

    public MemberVoteAlreadyComputedException() {
        super("Member already voted in this session");
    }

}
