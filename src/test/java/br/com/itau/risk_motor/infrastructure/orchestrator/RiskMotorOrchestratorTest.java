package br.com.itau.risk_motor.infrastructure.orchestrator;

import br.com.itau.risk_motor.core.orchestrator.Orchestrator;
import br.com.itau.risk_motor.domain.motor.ScoreRequest;
import br.com.itau.risk_motor.domain.motor.ScoreResponse;
import br.com.itau.risk_motor.infrastructure.rules.RulesExecutor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RiskMotorOrchestratorTest {

    @Mock
    private RulesExecutor motorDomainService;

    @InjectMocks
    private RiskMotorOrchestrator orchestrator;

    @Test
    void analyze_shouldCalculateAndReturnScore_whenNoDenies() {
        ScoreRequest request = new ScoreRequest();
        request.setCpfAllow(true);
        request.setCpfDeny(false);
        request.setIpDeny(false);
        request.setDeviceDeny(false);
        request.setTxType("PIX");
        request.setTxValue(BigDecimal.valueOf(100.00));

        when(motorDomainService.calculateScore(true, false, false, false, "PIX", BigDecimal.valueOf(100.00))).thenReturn(80);

        ScoreResponse response = orchestrator.analyze(request);

        assertThat(response.getScore()).isEqualTo(80);
        verify(motorDomainService).calculateScore(true, false, false, false, "PIX", BigDecimal.valueOf(100.00));
    }

    @Test
    void analyze_shouldCalculateAndReturnScore_whenHasDenies() {
        ScoreRequest request = new ScoreRequest();
        request.setCpfAllow(false);
        request.setCpfDeny(true);
        request.setIpDeny(true);
        request.setDeviceDeny(true);
        request.setTxType("TED");
        request.setTxValue(BigDecimal.valueOf(5000.00));

        when(motorDomainService.calculateScore(anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), anyString(), any(BigDecimal.class))).thenReturn(20);

        ScoreResponse response = orchestrator.analyze(request);

        assertThat(response.getScore()).isEqualTo(20);
        verify(motorDomainService).calculateScore(false, true, true, true, "TED", BigDecimal.valueOf(5000.00));
    }
}