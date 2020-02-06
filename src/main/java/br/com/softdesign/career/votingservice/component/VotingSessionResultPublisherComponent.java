package br.com.softdesign.career.votingservice.component;

import br.com.softdesign.career.votingservice.domain.VotingSessionResult;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class VotingSessionResultPublisherComponent {

    private final AmqpTemplate amqpTemplate;

    private final String exchange;

    private final String routingKey;

    public VotingSessionResultPublisherComponent(final AmqpTemplate amqpTemplate,
                                                 @Value("${amqp.exchange}") final String exchange,
                                                 @Value("${amqp.routingKey}") final String routingKey) {
        this.amqpTemplate = amqpTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public void publish(final VotingSessionResult votingSessionResult) {
        amqpTemplate.convertAndSend(exchange, routingKey, votingSessionResult);
    }

}
