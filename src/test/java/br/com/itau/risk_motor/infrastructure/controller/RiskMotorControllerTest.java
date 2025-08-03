package br.com.itau.risk_motor.infrastructure.controller;

import br.com.itau.risk_motor.domain.motor.ScoreRequest;
import br.com.itau.risk_motor.domain.motor.ScoreResponse;
import br.com.itau.risk_motor.infrastructure.orchestrator.RiskMotorOrchestrator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RiskMotorController.class)
class RiskMotorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RiskMotorOrchestrator orchestrator;

    @Test
    @DisplayName("POST /api/motor/analyze -> deve retornar score com sucesso")
    void analyze_shouldReturnScoreSuccessfully() throws Exception {
        ScoreResponse response = new ScoreResponse();
        response.setScore(320);

        when(orchestrator.analyze(any(ScoreRequest.class)))
                .thenReturn(response);

        String jsonRequest = """
            {
              "cpf": "12345678900",
              "ip": "10.0.0.1",
              "deviceId": "550e8400-e29b-41d4-a716-446655440000",
              "txType": "PIX",
              "txValue": 100.0,
              "cpfAllow": false,
              "cpfDeny": false,
              "ipDeny": false,
              "deviceDeny": false
            }
        """;

        mockMvc.perform(post("/api/motor/analyze")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.score").value(320));
    }

    @Test
    @DisplayName("POST /api/motor/analyze -> requisição malformada deve retornar 400")
    void analyze_invalidRequest_shouldReturnBadRequest() throws Exception {
        String invalidJson = """
            { "cpf":, "deviceId": null }
        """;

        mockMvc.perform(post("/api/motor/analyze")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}