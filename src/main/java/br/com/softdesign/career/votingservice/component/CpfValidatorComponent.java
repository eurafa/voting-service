package br.com.softdesign.career.votingservice.component;

import br.com.softdesign.career.votingservice.enums.MemberCpfValidationStatus;
import br.com.softdesign.career.votingservice.to.MemberCpfValidationTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Objects;

@Component
public class CpfValidatorComponent {

    @Value("${cpfValidator.api.url}")
    private String cpfValidatorApiUrl;

    private final RestTemplate restTemplate;

    public CpfValidatorComponent(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public MemberCpfValidationStatus validateCpf(final String cpf) {
        final MemberCpfValidationTO memberCpfValidationTO = restTemplate.getForObject(URI.create(cpfValidatorApiUrl + "/" + cpf), MemberCpfValidationTO.class);
        return Objects.requireNonNull(memberCpfValidationTO).getStatus();
    }

}
