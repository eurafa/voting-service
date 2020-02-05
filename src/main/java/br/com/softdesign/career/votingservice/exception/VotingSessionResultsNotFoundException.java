package br.com.softdesign.career.votingservice.exception;

public class VotingSessionResultsNotFoundException extends RuntimeException {

    public VotingSessionResultsNotFoundException() {
        super("Voting session results not found");
    }

}
