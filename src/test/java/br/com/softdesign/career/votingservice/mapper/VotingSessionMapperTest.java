package br.com.softdesign.career.votingservice.mapper;

import br.com.softdesign.career.votingservice.domain.VotingSession;
import br.com.softdesign.career.votingservice.to.OpenVotingSessionTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VotingSessionMapperTest {

    @Test
    void testConstructor() {
        // Given
        final VotingSessionMapper mapper;

        // When
        mapper = new VotingSessionMapper();

        // Then
        assertThat(mapper).isNotNull();
    }

    @Test
    void toModel() {
        // Given
        final OpenVotingSessionTO openVotingSessionTO = new OpenVotingSessionTO("agendaId", 1);

        // When
        final VotingSession votingSession = VotingSessionMapper.toModel(openVotingSessionTO);

        // Then
        assertThat(votingSession).isNotNull();
        assertThat(votingSession.getId()).isNotNull();
        assertThat(votingSession.getAgendaId()).isEqualTo(openVotingSessionTO.getAgendaId());
        assertThat(votingSession.getStart()).isNotNull();
        assertThat(votingSession.getEnd()).isNotNull();
        assertThat(votingSession.getEnd()).isEqualTo(votingSession.getStart().plusMinutes(openVotingSessionTO.getDurationInMinutes()));
    }

    @Test
    void toModelDefaultDuration() {
        // Given
        final OpenVotingSessionTO openVotingSessionTO = new OpenVotingSessionTO("agendaId");

        // When
        final VotingSession votingSession = VotingSessionMapper.toModel(openVotingSessionTO);

        // Then
        assertThat(votingSession).isNotNull();
        assertThat(votingSession.getId()).isNotNull();
        assertThat(votingSession.getAgendaId()).isEqualTo(openVotingSessionTO.getAgendaId());
        assertThat(votingSession.getStart()).isNotNull();
        assertThat(votingSession.getEnd()).isNotNull();
        assertThat(votingSession.getEnd()).isEqualTo(votingSession.getStart().plusMinutes(VotingSessionMapper.DEFAULT_DURATION));
    }

    @Test
    void toModelFailure() {
        // Given

        // When
        final Throwable throwable = Assertions.catchThrowable(() -> VotingSessionMapper.toModel(null));

        // Then
        assertThat(throwable).isInstanceOf(NullPointerException.class);
    }

}