package br.com.softdesign.career.votingservice.to;

public class VotingAgendaTO {

    private String title;

    private String description;

    public VotingAgendaTO(final String title, final String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

}
