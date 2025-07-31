package br.com.itau.risk_motor.domain.motor;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ScoreRequest {
    private String cpf;
    private String ip;
    private UUID deviceId;
    private String txType;
    private BigDecimal txValue;
    private boolean cpfAllow;
    private boolean cpfDeny;
    private boolean ipDeny;
    private boolean deviceDeny;
}