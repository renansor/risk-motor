package br.com.itau.risk_motor.core.helper;

import org.springframework.stereotype.Service;

@Service
public class RulesDomainService {

    public void validateScriptName(String name) {
        if (name == null || !name.endsWith(".groovy")) {
            throw new IllegalArgumentException("script must be .groovy");
        }

        if (name.contains("..") || name.contains("/")) {
            throw new IllegalArgumentException("script name cannot have special characters");
        }
    }

    public void validateScriptContent(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("script content cannot be empty");
        }
    }
}