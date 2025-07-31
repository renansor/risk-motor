package br.com.itau.risk_motor.infrastructure.controller;

import br.com.itau.risk_motor.domain.motor.ScoreRequest;
import br.com.itau.risk_motor.domain.motor.ScoreResponse;
import br.com.itau.risk_motor.core.orchestrator.MotorApplicationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/motor")
@Log4j2
public class RiskMotorController {

    private final MotorApplicationService service;

    public RiskMotorController(MotorApplicationService service) {
        this.service = service;
    }

    @PostMapping("/analyze")
    public ResponseEntity<ScoreResponse> analyze(@RequestBody ScoreRequest request) {
        log.info("received request: {}", request);
        return ResponseEntity.ok(service.analyze(request));
    }
}