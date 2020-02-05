package br.com.softdesign.career.votingservice.service;

import br.com.softdesign.career.votingservice.domain.MemberVote;
import br.com.softdesign.career.votingservice.domain.VotingAgenda;
import br.com.softdesign.career.votingservice.domain.VotingSession;
import br.com.softdesign.career.votingservice.exception.VotingAgendaNotFoundException;
import br.com.softdesign.career.votingservice.exception.VotingSessionClosedException;
import br.com.softdesign.career.votingservice.exception.VotingSessionNotFoundException;
import br.com.softdesign.career.votingservice.mapper.VotingSessionMapper;
import br.com.softdesign.career.votingservice.repository.VotingAgendaRepository;
import br.com.softdesign.career.votingservice.repository.VotingSessionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

import java.time.LocalDateTime;

@Service
public class VotingSessionService {

    private final VotingSessionRepository repository;

    private final VotingAgendaRepository agendaRepository;

    public VotingSessionService(final VotingSessionRepository repository, final VotingAgendaRepository agendaRepository) {
        this.repository = repository;
        this.agendaRepository = agendaRepository;
    }

    public Mono<VotingSession> openVotingSession(final VotingSession votingSession) {
        final Mono<VotingAgenda> votingAgendaMono = agendaRepository.findById(votingSession.getAgendaId());
        return votingAgendaMono
                .flatMap(votingAgenda -> this.repository.save(votingSession))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new VotingAgendaNotFoundException())));
    }

    public Mono<VotingSession> computeMemberVote(final String votingSessionId, final MemberVote memberVote) {
        final Mono<VotingSession> votingSessionMono = repository.findById(votingSessionId);
        return votingSessionMono
                .switchIfEmpty(Mono.defer(() -> Mono.error(new VotingSessionNotFoundException())))
                .handle(this::computeMemberVoteHandler)
                .flatMap(votingSession -> this.repository.save(VotingSessionMapper.pushMemberVote(votingSession, memberVote)));
    }

    private void computeMemberVoteHandler(final VotingSession votingSession, final SynchronousSink<VotingSession> sink) {
        final boolean isOpened = LocalDateTime.now().isBefore(votingSession.getEnd());
        if (isOpened) {
            sink.next(votingSession);
        } else {
            sink.error(new VotingSessionClosedException());
        }
    }

}
