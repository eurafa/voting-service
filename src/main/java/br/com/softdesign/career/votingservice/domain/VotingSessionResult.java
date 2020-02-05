package br.com.softdesign.career.votingservice.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "voting-session-result")
public class VotingSessionResult {

    @Id
    private String id;

    private Map<String, Long> result;

    public VotingSessionResult() {
    }

    public VotingSessionResult(final String id, final Map<String, Long> result) {
        this.id = id;
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Long> getResult() {
        return result;
    }

    public void setResult(Map<String, Long> result) {
        this.result = result;
    }

}
