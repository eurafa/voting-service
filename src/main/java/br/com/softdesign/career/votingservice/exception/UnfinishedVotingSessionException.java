package br.com.softdesign.career.votingservice.exception;

public class UnfinishedVotingSessionException extends RuntimeException {

    public UnfinishedVotingSessionException() {
        super("Voting session is in progress");
    }

}
