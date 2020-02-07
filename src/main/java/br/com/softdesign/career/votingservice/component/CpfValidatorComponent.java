package br.com.softdesign.career.votingservice.component;

import br.com.softdesign.career.votingservice.enums.CpfValidationStatus;
import br.com.softdesign.career.votingservice.exception.InvalidCpfException;
import br.com.softdesign.career.votingservice.to.CpfValidationResponseTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class CpfValidatorComponent {

    private final String cpfValidatorApiUrl;

    public CpfValidatorComponent(@Value("${cpfValidator.api.url}") final String cpfValidatorApiUrl) {
        this.cpfValidatorApiUrl = cpfValidatorApiUrl;
    }

    public Mono<CpfValidationStatus> validateCpf(final String cpf) {
        final Mono<CpfValidationResponseTO> cpfValidationResponseMono = WebClient.create()
                .get()
                .uri(cpfValidatorApiUrl + "/" + cpf)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> Mono.defer(() -> Mono.error(new InvalidCpfException(cpf))))
                .bodyToMono(CpfValidationResponseTO.class);
        return cpfValidationResponseMono.map(CpfValidationResponseTO::getStatus);
    }

}
