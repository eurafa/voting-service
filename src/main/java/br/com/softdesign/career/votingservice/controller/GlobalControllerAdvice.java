package br.com.softdesign.career.votingservice.controller;

import br.com.softdesign.career.votingservice.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = VotingAgendaNotFoundException.class)
    protected Mono<ResponseEntity<?>> handleException(final VotingAgendaNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return Mono.just(ResponseEntity.notFound().build());
    }

    @ExceptionHandler(value = VotingSessionNotFoundException.class)
    protected Mono<ResponseEntity<?>> handleException(final VotingSessionNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return Mono.just(ResponseEntity.notFound().build());
    }

    @ExceptionHandler(value = VotingSessionClosedException.class)
    protected Mono<ResponseEntity<?>> handleException(final VotingSessionClosedException ex) {
        log.error(ex.getMessage(), ex);
        return Mono.just(ResponseEntity.status(HttpStatus.PRECONDITION_REQUIRED).body(ex.getMessage()));
    }

    @ExceptionHandler(value = MemberVoteAlreadyComputedException.class)
    protected Mono<ResponseEntity<?>> handleException(final MemberVoteAlreadyComputedException ex) {
        log.error(ex.getMessage(), ex);
        return Mono.just(ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(ex.getMessage()));
    }

    @ExceptionHandler(value = UnfinishedVotingSessionException.class)
    protected Mono<ResponseEntity<?>> handleException(final UnfinishedVotingSessionException ex) {
        log.error(ex.getMessage(), ex);
        return Mono.just(ResponseEntity.status(HttpStatus.PRECONDITION_REQUIRED).body(ex.getMessage()));
    }

    @ExceptionHandler(value = VotingSessionResultsNotFoundException.class)
    protected Mono<ResponseEntity<?>> handleException(final VotingSessionResultsNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return Mono.just(ResponseEntity.notFound().build());
    }

}
