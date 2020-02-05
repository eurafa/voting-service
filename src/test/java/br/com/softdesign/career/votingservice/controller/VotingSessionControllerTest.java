package br.com.softdesign.career.votingservice.controller;

import br.com.softdesign.career.votingservice.domain.MemberVote;
import br.com.softdesign.career.votingservice.domain.VotingSession;
import br.com.softdesign.career.votingservice.enums.Vote;
import br.com.softdesign.career.votingservice.exception.VotingAgendaNotFoundException;
import br.com.softdesign.career.votingservice.exception.VotingSessionClosedException;
import br.com.softdesign.career.votingservice.exception.VotingSessionNotFoundException;
import br.com.softdesign.career.votingservice.mapper.MemberVoteMapper;
import br.com.softdesign.career.votingservice.mapper.VotingSessionMapper;
import br.com.softdesign.career.votingservice.service.VotingSessionService;
import br.com.softdesign.career.votingservice.to.MemberVoteTO;
import br.com.softdesign.career.votingservice.to.OpenVotingSessionTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

    @Test
    public void computeMemberVote() {
        // Given
        final String sessionId = "sessionId";
        final MemberVoteTO memberVoteTO = new MemberVoteTO(sessionId, "memberId", Vote.YES);
        final MemberVote memberVote = MemberVoteMapper.toModel(memberVoteTO);
        final VotingSession votingSession = new VotingSession(sessionId, "agendaId", LocalDateTime.now().minusMinutes(1), LocalDateTime.now().plusMinutes(1), Collections.singleton(memberVote));
        given(service.computeMemberVote(anyString(), any())).willReturn(Mono.just(votingSession));

        // When
        final WebTestClient.ResponseSpec response = webTestClient.patch()
                .uri(VotingSessionController.URL_PATTERN)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(Mono.just(memberVoteTO), MemberVoteTO.class)
                .exchange();

        // Then
        response.expectStatus().isNoContent();
    }

    @Test
    public void computeMemberVoteFailureSessionNotFound() {
        // Given
        final MemberVoteTO memberVoteTO = new MemberVoteTO("sessionId", "memberId", Vote.YES);
        given(service.computeMemberVote(anyString(), any())).willReturn(Mono.error(new VotingSessionNotFoundException()));

        // When
        final WebTestClient.ResponseSpec response = webTestClient.patch()
                .uri(VotingSessionController.URL_PATTERN)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(Mono.just(memberVoteTO), MemberVoteTO.class)
                .exchange();

        // Then
        response.expectStatus().isBadRequest();
    }

    @Test
    public void computeMemberVoteFailureSessionClosed() {
        // Given
        final MemberVoteTO memberVoteTO = new MemberVoteTO("sessionId", "memberId", Vote.YES);
        given(service.computeMemberVote(anyString(), any())).willReturn(Mono.error(new VotingSessionClosedException()));

        // When
        final WebTestClient.ResponseSpec response = webTestClient.patch()
                .uri(VotingSessionController.URL_PATTERN)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(Mono.just(memberVoteTO), MemberVoteTO.class)
                .exchange();

        // Then
        response.expectStatus().isBadRequest();
    }

}