package br.com.softdesign.career.votingservice.to;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "Resultado da votação de uma sessão de uma pauta")
public class ResultsTO {

    private String sessionId;

    private Long totalVotesYes;

    private Long totalVotesNo;

    public ResultsTO() {
    }

    public ResultsTO(final String sessionId, final Long totalVotesYes, final Long totalVotesNo) {
        this.sessionId = sessionId;
        this.totalVotesYes = totalVotesYes;
        this.totalVotesNo = totalVotesNo;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getTotalVotesYes() {
        return totalVotesYes;
    }

    public void setTotalVotesYes(Long totalVotesYes) {
        this.totalVotesYes = totalVotesYes;
    }

    public Long getTotalVotesNo() {
        return totalVotesNo;
    }

    public void setTotalVotesNo(Long totalVotesNo) {
        this.totalVotesNo = totalVotesNo;
    }

}
