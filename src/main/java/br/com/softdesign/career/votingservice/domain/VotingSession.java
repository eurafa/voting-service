package br.com.softdesign.career.votingservice.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

@Document(collection = "voting-session")
public class VotingSession {

    @Id
    private final String id;

    private final String agendaId;

    private final LocalDateTime start;

    private final LocalDateTime end;

    private final Set<MemberVote> votes;

    public VotingSession(final String id, final String agendaId, final LocalDateTime start, final LocalDateTime end) {
        this.id = id;
        this.agendaId = agendaId;
        this.start = start;
        this.end = end;
        this.votes = Collections.emptySet();
    }

    public VotingSession(final String id, final String agendaId, final LocalDateTime start, final LocalDateTime end, final Set<MemberVote> votes) {
        this.id = id;
        this.agendaId = agendaId;
        this.start = start;
        this.end = end;
        this.votes = votes;
    }

    public String getId() {
        return id;
    }

    public String getAgendaId() {
        return agendaId;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public Set<MemberVote> getVotes() {
        return votes;
    }

}
