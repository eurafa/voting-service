package br.com.softdesign.career.votingservice.component;

import br.com.softdesign.career.votingservice.domain.VotingSessionResult;
import br.com.softdesign.career.votingservice.enums.Vote;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.core.AmqpTemplate;

import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

class VotingSessionResultPublisherComponentTest {

    private final AmqpTemplate amqpTemplate = Mockito.mock(AmqpTemplate.class);

    private final VotingSessionResultPublisherComponent component = new VotingSessionResultPublisherComponent(amqpTemplate, "exchange", "routingKey");

    @Test
    void publish() {
        // Given
        final String exchange = "exchange";
        final String routingKey = "routingKey";
        final Map<String, Long> votes = Arrays.asList(Vote.values()).stream().collect(Collectors.toMap(Object::toString, vote -> 1L));
        final VotingSessionResult result = new VotingSessionResult("sessionId", votes);
        doNothing().when(amqpTemplate).convertAndSend(exchange, routingKey, result);

        // When
        this.component.publish(result);

        // Then
        Mockito.verify(amqpTemplate).convertAndSend(exchange, routingKey, result);
    }

}