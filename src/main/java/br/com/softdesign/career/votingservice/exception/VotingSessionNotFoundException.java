package br.com.softdesign.career.votingservice.exception;

public class VotingSessionNotFoundException extends RuntimeException {

    public VotingSessionNotFoundException(final String sessionId) {
        super(String.format("Voting session %s not found", sessionId));
    }

}
