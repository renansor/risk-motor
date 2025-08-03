package br.com.itau.risk_motor.core.rules;

public interface RulesValidator {
    void validateScriptName(String name);
    void validateScriptContent(String content);
}
