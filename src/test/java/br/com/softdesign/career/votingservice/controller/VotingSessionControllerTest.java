package br.com.softdesign.career.votingservice.controller;

import br.com.softdesign.career.votingservice.domain.VotingSession;
import br.com.softdesign.career.votingservice.exception.VotingAgendaNotFoundException;
import br.com.softdesign.career.votingservice.mapper.VotingSessionMapper;
import br.com.softdesign.career.votingservice.service.VotingSessionService;
import br.com.softdesign.career.votingservice.to.OpenVotingSessionTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VotingSessionControllerTest {

    @Autowired
    private ApplicationContext context;

    @MockBean
    private VotingSessionService service;

    private WebTestClient webTestClient;

    @BeforeEach
    public void setUp() {
        webTestClient = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    public void openVotingSession() {
        // Given
        final OpenVotingSessionTO openVotingSessionTO = new OpenVotingSessionTO("agendaId", 1);
        final VotingSession votingSession = VotingSessionMapper.toModel(openVotingSessionTO);
        given(service.openVotingSession(any())).willReturn(Mono.just(votingSession));

        // When
        final WebTestClient.ResponseSpec response = webTestClient.post()
                .uri(VotingSessionController.URL_PATTERN)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(Mono.just(openVotingSessionTO), OpenVotingSessionTO.class)
                .exchange();

        // Then
        response
                .expectStatus().isCreated()
                .expectHeader().value(LOCATION, location -> assertThat(location).isEqualTo(VotingSessionController.URL_PATTERN + "/" + votingSession.getId()));
    }

    @Test
    public void openVotingSessionFailureAgendaNotFound() {
        // Given
        final OpenVotingSessionTO openVotingSessionTO = new OpenVotingSessionTO("agendaId", 1);
        final VotingSession votingSession = VotingSessionMapper.toModel(openVotingSessionTO);
        given(service.openVotingSession(any())).willReturn(Mono.error(new VotingAgendaNotFoundException()));

        // When
        final WebTestClient.ResponseSpec response = webTestClient.post()
                .uri(VotingSessionController.URL_PATTERN)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(Mono.just(openVotingSessionTO), OpenVotingSessionTO.class)
                .exchange();

        // Then
        response.expectStatus().isBadRequest();
    }

}