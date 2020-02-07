package br.com.softdesign.career.votingservice.service;

import br.com.softdesign.career.votingservice.domain.MemberVote;
import br.com.softdesign.career.votingservice.domain.VotingAgenda;
import br.com.softdesign.career.votingservice.domain.VotingSession;
import br.com.softdesign.career.votingservice.exception.MemberVoteAlreadyComputedException;
import br.com.softdesign.career.votingservice.exception.VotingAgendaNotFoundException;
import br.com.softdesign.career.votingservice.exception.VotingSessionClosedException;
import br.com.softdesign.career.votingservice.exception.VotingSessionNotFoundException;
import br.com.softdesign.career.votingservice.repository.VotingAgendaRepository;
import br.com.softdesign.career.votingservice.repository.VotingSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class VotingSessionService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final VotingSessionRepository repository;

    private final VotingAgendaRepository agendaRepository;

    public VotingSessionService(final VotingSessionRepository repository, final VotingAgendaRepository agendaRepository) {
        this.repository = repository;
        this.agendaRepository = agendaRepository;
    }

    public Mono<VotingSession> openVotingSession(final VotingSession votingSession) {
        log.info("Open a session {} for agenda {}", votingSession.getId(), votingSession.getAgendaId());
        final Mono<VotingAgenda> votingAgendaMono = agendaRepository.findById(votingSession.getAgendaId());
        return votingAgendaMono
                .flatMap(votingAgenda -> this.repository.save(votingSession))
                .switchIfEmpty(Mono.defer(() -> Mono.error(() -> new VotingAgendaNotFoundException(votingSession.getAgendaId()))));
    }

    public Mono<VotingSession> computeMemberVote(final String votingSessionId, final MemberVote memberVote) {
        log.info("Computing a member {} vote for session {}", memberVote.getMemberId(), votingSessionId);
        final Mono<VotingSession> votingSessionMono = repository.findById(votingSessionId);
        return votingSessionMono
//                .handle((VotingSession votingSession, SynchronousSink<VotingSession> sink) -> computeMemberVoteHandler(votingSession, memberVote, sink))
                .flatMap(this::validateSession)
                .flatMap(votingSession -> this.validateMemberVote(votingSession, memberVote))
                .flatMap(votingSession -> this.repository.save(pushMemberVote(votingSession, memberVote)))
                .switchIfEmpty(Mono.defer(() -> Mono.error(() -> new VotingSessionNotFoundException(votingSessionId))));
    }

    private Mono<VotingSession> validateSession(final VotingSession votingSession) {
        return Mono.just(votingSession)
                .filter(vs -> LocalDateTime.now().isBefore(vs.getEnd()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(() -> new VotingSessionClosedException(votingSession.getId(), votingSession.getEnd()))));
    }

    private Mono<VotingSession> validateMemberVote(final VotingSession votingSession, final MemberVote memberVote) {
        return Mono.just(votingSession)
                .filter(vs -> canMemberVote(vs, memberVote))
                .switchIfEmpty(Mono.defer(() -> Mono.error(() -> new MemberVoteAlreadyComputedException(memberVote.getMemberId(), votingSession.getId()))));
    }

    private boolean canMemberVote(final VotingSession votingSession, final MemberVote memberVote) {
        final Set<MemberVote> memberVotes = Optional.ofNullable(votingSession.getVotes()).orElse(Collections.emptySet());
        final Set<String> membersAlreadyVoted = memberVotes.stream().map(MemberVote::getMemberId).collect(Collectors.toSet());
        return !membersAlreadyVoted.contains(memberVote.getMemberId());
    }

    VotingSession pushMemberVote(final VotingSession votingSession, final MemberVote memberVote) {
        return new VotingSession(
                votingSession.getId(),
                votingSession.getAgendaId(),
                votingSession.getStart(),
                votingSession.getEnd(),
                Optional.ofNullable(votingSession.getVotes())
                        .filter(votes -> !votes.isEmpty())
                        .map(votes -> Stream
                                .of(votes, Collections.singleton(memberVote))
                                .flatMap(Collection::stream)
                                .collect(Collectors.toSet()))
                        .orElseGet(() -> Collections.singleton(memberVote)));
    }

}
