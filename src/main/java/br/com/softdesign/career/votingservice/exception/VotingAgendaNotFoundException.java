package br.com.softdesign.career.votingservice.exception;

public class VotingAgendaNotFoundException extends RuntimeException {

    public VotingAgendaNotFoundException() {
        super("Voting agenda not found");
    }
}
