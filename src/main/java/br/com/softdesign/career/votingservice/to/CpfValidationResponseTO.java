package br.com.softdesign.career.votingservice.to;

import br.com.softdesign.career.votingservice.enums.CpfValidationStatus;

public class CpfValidationResponseTO {

    private CpfValidationStatus status;

    public CpfValidationResponseTO() {
    }

    public CpfValidationResponseTO(final CpfValidationStatus status) {
        this.status = status;
    }

    public CpfValidationStatus getStatus() {
        return status;
    }

    public void setStatus(final CpfValidationStatus status) {
        this.status = status;
    }

}
