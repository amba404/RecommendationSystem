package pro.sky.star.bank.recommendation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pro.sky.star.bank.recommendation.model.RuleSet;
import pro.sky.star.bank.recommendation.model.RuleStat;
import pro.sky.star.bank.recommendation.repository.RecommendedProductRepository;
import pro.sky.star.bank.recommendation.repository.RuleSetRepository;
import pro.sky.star.bank.recommendation.repository.RuleStatRepository;
import pro.sky.star.bank.recommendation.repository.TransactionsRepository;
import pro.sky.star.bank.recommendation.service.interfaces.RuleService;
import pro.sky.star.bank.recommendation.service.interfaces.RuleStatService;

import java.util.List;
import java.util.UUID;

@WebMvcTest(controllers = {RuleController.class, RuleService.class, RuleStatService.class})
@ExtendWith(MockitoExtension.class)
class TestRuleControllerMvc {

    private final static UUID UUID_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private final static String jsonRequest = """
            {
                "product_name": "Название продукта",
                "product_id": "00000000-0000-0000-0000-000000000001",
                "product_text": "<текст рекомендации>",
                "rule": [
                    {
                        "query": "USER_OF",
                         "arguments": [
                            "CREDIT"
                        ]
                    }
                ]
            }
            """;
    private static RuleSet ruleSet;
    private static JSONObject jsonObject;
    private static RuleStat ruleStat;
    @MockitoBean
    RuleSetRepository rulesetRepository;
    @MockitoBean
    RuleStatRepository ruleStatRepository;
    @MockitoBean
    RecommendedProductRepository productRepository;
    @MockitoBean
    TransactionsRepository transactionsRepository;
    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void init() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        ruleSet = mapper.readValue(jsonRequest, RuleSet.class);
        ruleSet.setId(UUID_ID);

        ruleStat = new RuleStat();
        ruleStat.setId(UUID_ID);
        ruleStat.setRulesetCount(10);

        jsonObject = new JSONObject(jsonRequest);

    }

    @Test
    void createRule() throws Exception {

        Mockito.when(rulesetRepository.save(Mockito.any(RuleSet.class))).thenReturn(ruleSet);
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/rule") //send
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(UUID_ID.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rule").isNotEmpty());

    }

    @Test
    void getAllRuleSets() throws Exception {
        Mockito.when(rulesetRepository.findAll()).thenReturn(List.of(ruleSet));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/rule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }

    @Test
    void deleteRuleSet() throws Exception {
        Mockito.doNothing().when(rulesetRepository).deleteById(Mockito.any(UUID.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/rule/{id}", UUID_ID.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void getRuleStats() throws Exception {
        Mockito.when(ruleStatRepository.findAll()).thenReturn(List.of(ruleStat));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/rule/stats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.stats").isNotEmpty());
    }
}