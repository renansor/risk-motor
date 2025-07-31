package br.com.itau.risk_motor.core;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

@Service
@Log4j2
public class MotorDomainService {

    private final ResourceLoader resourceLoader;
    private final String rulesBasePath;

    public MotorDomainService(ResourceLoader resourceLoader,
                              @Value("${rules.path:classpath:rules}") String rulesBasePath) {
        this.resourceLoader = resourceLoader;
        this.rulesBasePath = rulesBasePath.endsWith("/") ? rulesBasePath : rulesBasePath + "/";
    }

    public int calculateScore(boolean cpfAllow, boolean cpfDeny, boolean ipDeny, boolean deviceDeny, String txType, BigDecimal txValue) {
        Binding binding = new Binding();
        binding.setVariable("cpfAllow", cpfAllow);
        binding.setVariable("cpfDeny", cpfDeny);
        binding.setVariable("ipDeny", ipDeny);
        binding.setVariable("deviceDeny", deviceDeny);
        binding.setVariable("txType", txType);
        binding.setVariable("txValue", txValue);

        log.info("setted values {}", binding);

        GroovyShell shell = new GroovyShell(binding);

        log.info("it will execute default rules");

        int defaultScore = executeScript(shell, rulesBasePath, "default.groovy");

        log.info("it will execute txType rules");
        int specificScore = 0;
        if (txType != null && !txType.isEmpty()) {
            String specificFile = txType.toLowerCase() + ".groovy";
            Resource specificResource = resourceLoader.getResource(rulesBasePath + specificFile);
            if (specificResource.exists()) {
                specificScore = executeScript(shell, rulesBasePath, specificFile);
                log.info("executed txType {}: score = {}", txType, specificScore);
            } else {
                log.warn("txType rule not found for {}: {}", txType, specificFile);
            }
        }

        int totalScore = defaultScore + specificScore;
        log.info("total score: {}", totalScore);
        return totalScore;
    }

    private int executeScript(GroovyShell shell, String path, String file) {
        String script;
        try {
            Resource resource = resourceLoader.getResource(path+file);
            if (!resource.exists()) {
                throw new IOException("Resource not found: " + path+file);
            }
            script = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            Object result = shell.evaluate(script);
            if (result == null) {
                throw new RuntimeException("script " + path+file + " returned null; has script return?");
            }
            return ((Number) result).intValue();  // Mais flexível com Number
        } catch (IOException e) {
            throw new RuntimeException("fail to load groovy script: " + path+file, e);
        }
    }
}