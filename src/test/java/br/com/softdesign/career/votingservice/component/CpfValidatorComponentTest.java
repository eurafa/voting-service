package br.com.softdesign.career.votingservice.component;

import br.com.softdesign.career.votingservice.enums.MemberCpfValidationStatus;
import br.com.softdesign.career.votingservice.to.MemberCpfValidationTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class CpfValidatorComponentTest {

    private final RestTemplate restTemplate = Mockito.mock(RestTemplate.class);

    private final CpfValidatorComponent component = new CpfValidatorComponent(restTemplate);

    @Test
    void validateCpf() {
        // Given
        final String cpf = "cpf";
        final MemberCpfValidationStatus status = MemberCpfValidationStatus.ABLE_TO_VOTE;
        final MemberCpfValidationTO memberCpfValidationTO = new MemberCpfValidationTO(status);
        given(restTemplate.getForObject(any(), any())).willReturn(memberCpfValidationTO);

        // When
        final MemberCpfValidationStatus memberCpfValidationStatus = this.component.validateCpf(cpf);

        // Then
        assertThat(memberCpfValidationStatus).isEqualTo(status);
    }

    @Test
    void validateCpfFailureNull() {
        // Given
        final String cpf = "cpf";
        given(restTemplate.getForObject(any(), any())).willReturn(null);

        // When
        final Throwable throwable = catchThrowable(() -> this.component.validateCpf(cpf));

        // Then
        assertThat(throwable).isInstanceOf(NullPointerException.class);
    }

}