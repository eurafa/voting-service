package br.com.softdesign.career.votingservice.exception;

public class VotingSessionResultsNotFoundException extends RuntimeException {

    public VotingSessionResultsNotFoundException(final String sessionId) {
        super(String.format("Voting session %s results not found", sessionId));
    }

}
