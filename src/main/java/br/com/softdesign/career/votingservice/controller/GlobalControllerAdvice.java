package br.com.softdesign.career.votingservice.controller;

import br.com.softdesign.career.votingservice.exception.UnfinishedVotingSessionException;
import br.com.softdesign.career.votingservice.exception.VotingAgendaNotFoundException;
import br.com.softdesign.career.votingservice.exception.VotingSessionClosedException;
import br.com.softdesign.career.votingservice.exception.VotingSessionNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(value = VotingAgendaNotFoundException.class)
    protected Mono<ResponseEntity<?>> handleException(final VotingAgendaNotFoundException ex) {
        return Mono.just(ResponseEntity.badRequest().body(ex.getMessage()));
    }

    @ExceptionHandler(value = VotingSessionNotFoundException.class)
    protected Mono<ResponseEntity<?>> handleException(final VotingSessionNotFoundException ex) {
        return Mono.just(ResponseEntity.badRequest().body(ex.getMessage()));
    }

    @ExceptionHandler(value = VotingSessionClosedException.class)
    protected Mono<ResponseEntity<?>> handleException(final VotingSessionClosedException ex) {
        return Mono.just(ResponseEntity.badRequest().body(ex.getMessage()));
    }

    @ExceptionHandler(value = UnfinishedVotingSessionException.class)
    protected Mono<ResponseEntity<?>> handleException(final UnfinishedVotingSessionException ex) {
        return Mono.just(ResponseEntity.badRequest().body(ex.getMessage()));
    }

}
