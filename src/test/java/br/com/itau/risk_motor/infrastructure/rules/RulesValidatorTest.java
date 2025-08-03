package br.com.itau.risk_motor.infrastructure.rules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RulesValidatorTest {

    private final RulesValidator rulesValidator = new RulesValidator();

    @Test
    void validateScriptName_shouldNotThrow_whenValidName() {
        assertDoesNotThrow(() -> rulesValidator.validateScriptName("valid.groovy"));
    }

    @Test
    void validateScriptName_shouldThrow_whenNameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> rulesValidator.validateScriptName(null), "script must be .groovy");
    }

    @Test
    void validateScriptName_shouldThrow_whenNameDoesNotEndWithGroovy() {
        assertThrows(IllegalArgumentException.class, () -> rulesValidator.validateScriptName("invalid.txt"), "script must be .groovy");
    }

    @Test
    void validateScriptName_shouldThrow_whenNameContainsSlash() {
        assertThrows(IllegalArgumentException.class, () -> rulesValidator.validateScriptName("invalid/name.groovy"), "script name cannot have special characters");
    }

    @Test
    void validateScriptName_shouldThrow_whenNameContainsDotDot() {
        assertThrows(IllegalArgumentException.class, () -> rulesValidator.validateScriptName("invalid..groovy"), "script name cannot have special characters");
    }

    @Test
    void validateScriptContent_shouldNotThrow_whenValidContent() {
        assertDoesNotThrow(() -> rulesValidator.validateScriptContent("return 100"));
    }

    @Test
    void validateScriptContent_shouldThrow_whenContentIsNull() {
        assertThrows(IllegalArgumentException.class, () -> rulesValidator.validateScriptContent(null), "script content cannot be empty");
    }

    @Test
    void validateScriptContent_shouldThrow_whenContentIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> rulesValidator.validateScriptContent(""), "script content cannot be empty");
    }

    @Test
    void validateScriptContent_shouldThrow_whenContentIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> rulesValidator.validateScriptContent("   "), "script content cannot be empty");
    }
}