package br.com.softdesign.career.votingservice.mapper;

import br.com.softdesign.career.votingservice.domain.VotingAgenda;
import br.com.softdesign.career.votingservice.to.VotingAgendaTO;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class VotingAgendaMapper {

    public static VotingAgenda toModel(final VotingAgendaTO to) {
        Objects.requireNonNull(to, "Transfer Object cannot be null");
        return new VotingAgenda(UUID.randomUUID().toString(), to.getTitle(), to.getDescription(), LocalDateTime.now());
    }

}
