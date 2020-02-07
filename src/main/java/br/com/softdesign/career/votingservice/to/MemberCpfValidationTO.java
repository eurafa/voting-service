package br.com.softdesign.career.votingservice.to;

import br.com.softdesign.career.votingservice.enums.CpfValidationStatus;

public class MemberCpfValidationTO {

    private CpfValidationStatus status;

    public MemberCpfValidationTO() {
    }

    public MemberCpfValidationTO(final CpfValidationStatus status) {
        this.status = status;
    }

    public CpfValidationStatus getStatus() {
        return status;
    }

    public void setStatus(CpfValidationStatus status) {
        this.status = status;
    }

}
