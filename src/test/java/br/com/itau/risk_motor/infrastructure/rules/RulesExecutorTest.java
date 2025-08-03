package br.com.itau.risk_motor.infrastructure.rules;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RulesExecutorTest {

    @Mock
    private ResourceLoader resourceLoader;

    private RulesExecutor rulesExecutor;

    @BeforeEach
    void setUp() {
        rulesExecutor = new RulesExecutor(resourceLoader, "classpath:rules");
    }

    @Test
    void calculateScore_shouldReturnDefaultScore_whenNoSpecificTxTypeScriptExists() throws IOException {
        String defaultScript = "return 100";
        mockResource("classpath:rules/default.groovy", defaultScript, true);
        mockResource("classpath:rules/pix.groovy", null, false);

        int score = rulesExecutor.calculateScore(true, false, false, false, "PIX", BigDecimal.valueOf(100));

        assertThat(score).isEqualTo(100);
    }

    @Test
    void calculateScore_shouldReturnSumOfDefaultAndSpecificScore_whenSpecificTxTypeScriptExists() throws IOException {
        String defaultScript = "return 100";
        String specificScript = "return 50";
        mockResource("classpath:rules/default.groovy", defaultScript, true);
        mockResource("classpath:rules/pix.groovy", specificScript, true);

        int score = rulesExecutor.calculateScore(true, false, false, false, "PIX", BigDecimal.valueOf(100));

        assertThat(score).isEqualTo(150);
    }

    @Test
    void calculateScore_shouldHandleNullTxType_withoutSpecificScript() throws IOException {
        String defaultScript = "return 100";
        mockResource("classpath:rules/default.groovy", defaultScript, true);

        int score = rulesExecutor.calculateScore(true, false, false, false, null, BigDecimal.valueOf(100));

        assertThat(score).isEqualTo(100);
    }

    @Test
    void calculateScore_shouldUseVariablesInScripts() throws IOException {
        String defaultScript = "if (cpfDeny) return 0 else return 100";
        String specificScript = "if (txValue > 50) return 50 else return 0";
        mockResource("classpath:rules/default.groovy", defaultScript, true);
        mockResource("classpath:rules/pix.groovy", specificScript, true);

        int score = rulesExecutor.calculateScore(true, false, false, false, "PIX", BigDecimal.valueOf(100));

        assertThat(score).isEqualTo(150);
    }

    @Test
    void executeScript_shouldThrowException_whenResourceNotFound() throws IOException {
        Resource mockResource = mock(Resource.class);
        when(resourceLoader.getResource(anyString())).thenReturn(mockResource);
        when(mockResource.exists()).thenReturn(false);

        assertThrows(RuntimeException.class, () -> rulesExecutor.executeScript(new GroovyShell(new Binding()), "classpath:rules/", "missing.groovy"));
    }

    @Test
    void executeScript_shouldThrowException_whenScriptReturnsNull() throws IOException {
        String scriptContent = "// no return";
        mockResource("classpath:rules/test.groovy", scriptContent, true);

        GroovyShell shell = new GroovyShell(new Binding());
        assertThrows(RuntimeException.class, () -> rulesExecutor.executeScript(shell, "classpath:rules/", "test.groovy"));
    }

    @Test
    void executeScript_shouldThrowException_whenScriptReturnsNonNumber() throws IOException {
        String scriptContent = "return 'not a number'";
        mockResource("classpath:rules/test.groovy", scriptContent, true);

        GroovyShell shell = new GroovyShell(new Binding());
        assertThrows(ClassCastException.class, () -> rulesExecutor.executeScript(shell, "classpath:rules/", "test.groovy"));
    }

    private void mockResource(String path, String content, boolean exists) throws IOException {
        Resource mockResource = mock(Resource.class);
        when(resourceLoader.getResource(path)).thenReturn(mockResource);
        when(mockResource.exists()).thenReturn(exists);
        if (exists && content != null) {
            InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
            when(mockResource.getInputStream()).thenReturn(inputStream);
        }
    }
}