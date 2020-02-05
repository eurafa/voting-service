package br.com.softdesign.career.votingservice.exception;

public class VotingSessionClosedException extends RuntimeException {

    public VotingSessionClosedException() {
        super("Voting session is closed");
    }

}
