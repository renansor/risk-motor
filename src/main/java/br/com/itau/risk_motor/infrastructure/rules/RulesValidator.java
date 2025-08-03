package br.com.itau.risk_motor.infrastructure.rules;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class RulesValidator implements br.com.itau.risk_motor.core.rules.RulesValidator {

    public void validateScriptName(String name) {
        if (name == null || !name.endsWith(".groovy")) {
            log.error("scipt must be .groovy");
            throw new IllegalArgumentException("script must be .groovy");
        }

        if (name.contains("..") || name.contains("/")) {
            log.error("scipt name cannot have special characters");
            throw new IllegalArgumentException("script name cannot have special characters");
        }
    }

    public void validateScriptContent(String content) {
        if (content == null || content.isBlank()) {
            log.error("script content cannot be empty");
            throw new IllegalArgumentException("script content cannot be empty");
        }
    }
}