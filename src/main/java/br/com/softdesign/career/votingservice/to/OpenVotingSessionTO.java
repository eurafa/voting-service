package br.com.softdesign.career.votingservice.to;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("Abertura de sessão de uma pauta")
public class OpenVotingSessionTO {

    @ApiModelProperty("Identificador da pauta")
    private String agendaId;

    @ApiModelProperty("Duração em minutos para votação na sessão da pauta")
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
