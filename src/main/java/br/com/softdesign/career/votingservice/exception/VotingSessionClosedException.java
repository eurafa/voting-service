package br.com.softdesign.career.votingservice.exception;

import java.time.LocalDateTime;

public class VotingSessionClosedException extends RuntimeException {

    public VotingSessionClosedException(final String sessionId) {
        super(String.format("Voting session %s is closed", sessionId));
    }

    public VotingSessionClosedException(final String sessionId, final LocalDateTime sessionEnd) {
        super(String.format("Voting session %s is closed since %s", sessionId, sessionEnd));
    }

}
