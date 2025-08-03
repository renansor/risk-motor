package br.com.itau.risk_motor.core.orchestrator;

import br.com.itau.risk_motor.domain.motor.ScoreRequest;
import br.com.itau.risk_motor.domain.motor.ScoreResponse;

public interface Orchestrator {
    ScoreResponse analyze(ScoreRequest request);
}
