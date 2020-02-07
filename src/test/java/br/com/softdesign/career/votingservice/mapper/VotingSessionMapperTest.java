package br.com.softdesign.career.votingservice.mapper;

import br.com.softdesign.career.votingservice.domain.VotingSession;
import br.com.softdesign.career.votingservice.to.OpenVotingSessionTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VotingSessionMapperTest {

    private final VotingSessionMapper mapper = new VotingSessionMapper();

    @Test
    void map() {
        // Given
        final OpenVotingSessionTO openVotingSessionTO = new OpenVotingSessionTO("agendaId", 1);

        // When
        final VotingSession votingSession = mapper.map(openVotingSessionTO);

        // Then
        assertThat(votingSession).isNotNull();
        assertThat(votingSession.getId()).isNotNull();
        assertThat(votingSession.getAgendaId()).isEqualTo(openVotingSessionTO.getAgendaId());
        assertThat(votingSession.getStart()).isNotNull();
        assertThat(votingSession.getEnd()).isNotNull();
        assertThat(votingSession.getEnd()).isEqualTo(votingSession.getStart().plusMinutes(openVotingSessionTO.getDurationInMinutes()));
    }

    @Test
    void mapDefaultDuration() {
        // Given
        final OpenVotingSessionTO openVotingSessionTO = new OpenVotingSessionTO("agendaId");

        // When
        final VotingSession votingSession = mapper.map(openVotingSessionTO);

        // Then
        assertThat(votingSession).isNotNull();
        assertThat(votingSession.getId()).isNotNull();
        assertThat(votingSession.getAgendaId()).isEqualTo(openVotingSessionTO.getAgendaId());
        assertThat(votingSession.getStart()).isNotNull();
        assertThat(votingSession.getEnd()).isNotNull();
        assertThat(votingSession.getEnd()).isEqualTo(votingSession.getStart().plusMinutes(VotingSessionMapper.DEFAULT_DURATION));
    }

    @Test
    void mapFailure() {
        // Given

        // When
        final Throwable throwable = Assertions.catchThrowable(() -> mapper.map(null));

        // Then
        assertThat(throwable).isInstanceOf(NullPointerException.class);
    }

}