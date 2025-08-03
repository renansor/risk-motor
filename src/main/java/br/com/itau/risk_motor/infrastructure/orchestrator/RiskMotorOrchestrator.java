package br.com.itau.risk_motor.infrastructure.orchestrator;

import br.com.itau.risk_motor.core.orchestrator.Orchestrator;
import br.com.itau.risk_motor.infrastructure.rules.RulesExecutor;
import br.com.itau.risk_motor.domain.motor.ScoreRequest;
import br.com.itau.risk_motor.domain.motor.ScoreResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class RiskMotorOrchestrator implements Orchestrator {

    private final RulesExecutor motorDomainService;

    public RiskMotorOrchestrator(RulesExecutor motorDomainService) {
        this.motorDomainService = motorDomainService;
    }

    public ScoreResponse analyze(ScoreRequest request) {
        log.info("starting analyze");
        int score = motorDomainService.calculateScore(
                request.isCpfAllow(),
                request.isCpfDeny(),
                request.isIpDeny(),
                request.isDeviceDeny(),
                request.getTxType(),
                request.getTxValue()
        );
        ScoreResponse resp = new ScoreResponse();
        resp.setScore(score);
        return resp;
    }
}