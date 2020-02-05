package br.com.softdesign.career.votingservice.service;

import br.com.softdesign.career.votingservice.domain.MemberVote;
import br.com.softdesign.career.votingservice.domain.VotingAgenda;
import br.com.softdesign.career.votingservice.domain.VotingSession;
import br.com.softdesign.career.votingservice.exception.MemberVoteAlreadyComputedException;
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
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
                .handle((VotingSession votingSession, SynchronousSink<VotingSession> sink) -> computeMemberVoteHandler(votingSession, memberVote, sink))
                .flatMap(votingSession -> this.repository.save(VotingSessionMapper.pushMemberVote(votingSession, memberVote)))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new VotingSessionNotFoundException())));
    }

    private void computeMemberVoteHandler(final VotingSession votingSession, final MemberVote memberVote, final SynchronousSink<VotingSession> sink) {
        final boolean isOpened = LocalDateTime.now().isBefore(votingSession.getEnd());
        if (isOpened) {
            final Set<MemberVote> memberVotes = Optional.ofNullable(votingSession.getVotes()).orElse(Collections.emptySet());
            final Set<String> membersAlreadyVoted = memberVotes.stream().map(MemberVote::getMemberId).collect(Collectors.toSet());
            if (membersAlreadyVoted.contains(memberVote.getMemberId())) {
                sink.error(new MemberVoteAlreadyComputedException());
            } else {
                sink.next(votingSession);
            }
        } else {
            sink.error(new VotingSessionClosedException());
        }
    }

}
