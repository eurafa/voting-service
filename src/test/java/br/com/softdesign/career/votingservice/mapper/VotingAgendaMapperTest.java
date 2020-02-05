package br.com.softdesign.career.votingservice.mapper;

import br.com.softdesign.career.votingservice.domain.VotingAgenda;
import br.com.softdesign.career.votingservice.to.VotingAgendaTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VotingAgendaMapperTest {

    @Test
    void toModel() {
        // Given
        final VotingAgendaTO votingAgendaTO = new VotingAgendaTO("Title", "Description");

        // When
        final VotingAgenda votingAgenda = VotingAgendaMapper.toModel(votingAgendaTO);

        // Then
        assertThat(votingAgenda).isNotNull();
        assertThat(votingAgenda.getId()).isNotNull();
        assertThat(votingAgenda.getCreatedDate()).isNotNull();
        assertThat(votingAgenda.getTitle()).isEqualTo(votingAgendaTO.getTitle());
        assertThat(votingAgenda.getDescription()).isEqualTo(votingAgendaTO.getDescription());
    }

    @Test
    void toModelFailure() {
        // Given

        // When
        final Throwable votingAgenda = Assertions.catchThrowable(() -> VotingAgendaMapper.toModel(null));

        // Then
        assertThat(votingAgenda).isInstanceOf(NullPointerException.class);
    }

}