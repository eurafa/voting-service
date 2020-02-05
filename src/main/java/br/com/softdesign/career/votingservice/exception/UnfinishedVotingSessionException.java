package br.com.softdesign.career.votingservice.exception;

import java.time.LocalDateTime;

public class UnfinishedVotingSessionException extends RuntimeException {

    public UnfinishedVotingSessionException(final String sessionId) {
        super(String.format("Voting session %s is in progress", sessionId));
    }

    public UnfinishedVotingSessionException(final String sessionId, final LocalDateTime sessionEnd) {
        super(String.format("Voting session %s is in progress until %s", sessionId, sessionEnd));
    }

}
