package br.com.softdesign.career.votingservice.controller;

import br.com.softdesign.career.votingservice.exception.VotingAgendaNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(value = VotingAgendaNotFoundException.class)
    protected Mono<ResponseEntity<Object>> handleException(VotingAgendaNotFoundException ex) {
        return Mono.just(ResponseEntity.badRequest().body(ex.getMessage()));
    }

}
