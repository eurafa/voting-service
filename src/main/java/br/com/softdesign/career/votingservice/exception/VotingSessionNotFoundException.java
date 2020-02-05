package br.com.softdesign.career.votingservice.exception;

public class VotingSessionNotFoundException extends RuntimeException {

    public VotingSessionNotFoundException() {
        super("Voting session not found");
    }

}
