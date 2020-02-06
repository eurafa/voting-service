package br.com.softdesign.career.votingservice.controller;

import br.com.softdesign.career.votingservice.domain.VotingAgenda;
import br.com.softdesign.career.votingservice.mapper.VotingAgendaMapper;
import br.com.softdesign.career.votingservice.service.VotingAgendaService;
import br.com.softdesign.career.votingservice.to.VotingAgendaTO;
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
public class VotingAgendaControllerTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private VotingAgendaMapper mapper;

    @MockBean
    private VotingAgendaService service;

    private WebTestClient webTestClient;

    @BeforeEach
    public void setUp() {
        webTestClient = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    public void createVotingAgenda() {
        // Given
        final VotingAgendaTO votingAgendaTO = new VotingAgendaTO("Title", "Description");
        final VotingAgenda votingAgenda = mapper.map(votingAgendaTO);
        given(service.createVotingAgenda(any())).willReturn(Mono.just(votingAgenda));

        // When
        final WebTestClient.ResponseSpec response = webTestClient.post()
                .uri(VotingAgendaController.URL_PATTERN)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(Mono.just(votingAgendaTO), VotingAgendaTO.class)
                .exchange();

        // Then
        response
                .expectStatus().isCreated()
                .expectHeader().value(LOCATION, location -> assertThat(location).isEqualTo(VotingAgendaController.URL_PATTERN + "/" + votingAgenda.getId()));
    }

}