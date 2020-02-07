package br.com.softdesign.career.votingservice.exception;

public class InvalidCpfException extends RuntimeException {

    public InvalidCpfException(final String cpf) {
        super(String.format("CPF %s is NOT valid", cpf));
    }

}
