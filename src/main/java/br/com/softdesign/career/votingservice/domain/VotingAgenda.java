package br.com.softdesign.career.votingservice.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "voting-agenda")
public class VotingAgenda {

    @Id
    private final String id;

    private final String title;

    private final String description;

    private final LocalDateTime createdDate;

    public VotingAgenda(final String id, final String title, final String description, final LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

}
