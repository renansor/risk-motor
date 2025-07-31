/*package br.com.itau.risk_motor.infrastructure.controller;

import br.com.itau.risk_motor.core.RulesApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
public class RulesController {

    private final RulesApplicationService service;

    public RulesController(RulesApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<String>> listScripts() {
        return ResponseEntity.ok(service.listScripts());
    }

    @GetMapping("/{name}")
    public ResponseEntity<String> getScript(@PathVariable String name) {
        return ResponseEntity.ok(service.getScriptContent(name));
    }

    @PutMapping("/{name}")
    public ResponseEntity<Void> updateScript(@PathVariable String name, @RequestBody String content) {
        service.updateScript(name, content);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{name}")
    public ResponseEntity<Void> createScript(@PathVariable String name, @RequestBody String content) {
        service.createScript(name, content);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}*/