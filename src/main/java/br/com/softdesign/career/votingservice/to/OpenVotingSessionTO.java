package br.com.softdesign.career.votingservice.to;

public class OpenVotingSessionTO {

    private String agendaId;

    private Integer durationInMinutes;

    public OpenVotingSessionTO(final String agendaId) {
        this.agendaId = agendaId;
    }

    public OpenVotingSessionTO(final String agendaId, final Integer durationInMinutes) {
        this.agendaId = agendaId;
        this.durationInMinutes = durationInMinutes;
    }

    public String getAgendaId() {
        return agendaId;
    }

    public Integer getDurationInMinutes() {
        return durationInMinutes;
    }

}
