package br.com.softdesign.career.votingservice.controller;

import br.com.softdesign.career.votingservice.domain.VotingSessionResult;
import br.com.softdesign.career.votingservice.enums.Vote;
import br.com.softdesign.career.votingservice.exception.UnfinishedVotingSessionException;
import br.com.softdesign.career.votingservice.service.VotingSessionResultService;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VotingSessionResultControllerTest {

    @Autowired
    private ApplicationContext context;

    @MockBean
    private VotingSessionResultService service;

    private WebTestClient webTestClient;

    @BeforeEach
    public void setUp() {
        webTestClient = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    public void computeVotes() {
        // Given
        final String sessionId = "sessionId";
        final Map<String, Long> votes = Arrays.asList(Vote.values()).stream().collect(Collectors.toMap(Object::toString, v -> 1L));
        final VotingSessionResult votingSessionResult = new VotingSessionResult(sessionId, votes);
        given(service.computeVotes(anyString())).willReturn(Mono.just(votingSessionResult));

        // When
        final WebTestClient.ResponseSpec response = webTestClient.post()
                .uri(VotingSessionResultController.URL_PATTERN + "/" + sessionId)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .exchange();

        // Then
        response
                .expectStatus().isOk()
                .expectBody(VotingSessionResult.class);
    }

    @Test
    public void computeVotesFailureUnfinished() {
        // Given
        final String sessionId = "sessionId";
        given(service.computeVotes(anyString())).willReturn(Mono.error(new UnfinishedVotingSessionException()));

        // When
        final WebTestClient.ResponseSpec response = webTestClient.post()
                .uri(VotingSessionResultController.URL_PATTERN + "/" + sessionId)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .exchange();

        // Then
        response.expectStatus().isBadRequest();
    }

}