package br.com.softdesign.career.votingservice.to;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("Pauta de assembleia")
public class VotingAgendaTO {

    @ApiModelProperty("Titulo da pauta")
    private String title;

    @ApiModelProperty("Descrição da pauta")
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
