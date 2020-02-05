package br.com.softdesign.career.votingservice.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

@Document(collection = "voting-session")
public class VotingSession {

    @Id
    private String id;

    private String agendaId;

    private LocalDateTime start;

    private LocalDateTime end;

    private Set<MemberVote> votes;

    public VotingSession() {
    }

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

    public void setId(String id) {
        this.id = id;
    }

    public String getAgendaId() {
        return agendaId;
    }

    public void setAgendaId(String agendaId) {
        this.agendaId = agendaId;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public Set<MemberVote> getVotes() {
        return votes;
    }

    public void setVotes(Set<MemberVote> votes) {
        this.votes = votes;
    }

}
