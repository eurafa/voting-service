package br.com.softdesign.career.votingservice.to;

import br.com.softdesign.career.votingservice.enums.Vote;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class MemberVoteTO {

    @ApiModelProperty("Identificador de um membro da cooperativa")
    private String memberId;

    @ApiModelProperty("Voto do membro")
    private Vote vote;

    public MemberVoteTO(final String memberId, final Vote vote) {
        this.memberId = memberId;
        this.vote = vote;
    }

    public String getMemberId() {
        return memberId;
    }

    public Vote getVote() {
        return vote;
    }

}
