package br.com.itau.risk_motor.core;

import br.com.itau.risk_motor.domain.ScoreRequest;
import br.com.itau.risk_motor.domain.ScoreResponse;
import br.com.itau.risk_motor.core.MotorDomainService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class MotorApplicationService {

    private final MotorDomainService motorDomainService;

    public MotorApplicationService(MotorDomainService motorDomainService) {
        this.motorDomainService = motorDomainService;
    }

    public ScoreResponse analyze(ScoreRequest req) {
        log.info("starting analyze");
        int score = motorDomainService.calculateScore(
                req.isCpfAllow(),
                req.isCpfDeny(),
                req.isIpDeny(),
                req.isDeviceDeny(),
                req.getTxType(),
                req.getTxValue()
        );
        ScoreResponse resp = new ScoreResponse();
        resp.setScore(score);
        return resp;
    }
}