package br.com.softdesign.career.votingservice.to;

import br.com.softdesign.career.votingservice.enums.MemberCpfValidationStatus;

public class MemberCpfValidationTO {

    private MemberCpfValidationStatus status;

    public MemberCpfValidationTO() {
    }

    public MemberCpfValidationTO(final MemberCpfValidationStatus status) {
        this.status = status;
    }

    public MemberCpfValidationStatus getStatus() {
        return status;
    }

    public void setStatus(MemberCpfValidationStatus status) {
        this.status = status;
    }

}
