package br.com.softdesign.career.votingservice.mapper;

import br.com.softdesign.career.votingservice.domain.VotingSession;
import br.com.softdesign.career.votingservice.to.OpenVotingSessionTO;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class VotingSessionMapper {

    static final Integer DEFAULT_DURATION = 1;

    public static VotingSession toModel(final OpenVotingSessionTO to) {
        Objects.requireNonNull(to, "Transfer Object cannot be null");
        final LocalDateTime now = LocalDateTime.now();
        return new VotingSession(UUID.randomUUID().toString(),
                to.getAgendaId(),
                now,
                now.plusMinutes(Optional.ofNullable(to.getDurationInMinutes()).orElse(DEFAULT_DURATION)));
    }

}
