package br.com.softdesign.career.votingservice.exception;

public class VotingAgendaNotFoundException extends RuntimeException {

    public VotingAgendaNotFoundException(final String agendaId) {
        super(String.format("Voting agenda %s not found", agendaId));
    }
}
