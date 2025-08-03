package br.com.itau.risk_motor.core.rules;

import groovy.lang.GroovyShell;

import java.math.BigDecimal;

public interface RulesExecutor {
    int calculateScore (boolean cpfAllow, boolean cpfDeny, boolean ipDeny, boolean deviceDeny, String txType, BigDecimal txValue);
    int executeScript (GroovyShell shell, String path, String file);
}
