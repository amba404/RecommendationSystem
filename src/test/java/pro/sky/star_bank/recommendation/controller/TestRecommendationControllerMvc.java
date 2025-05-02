package pro.sky.star_bank.recommendation.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pro.sky.star_bank.recommendation.model.Recommendation;

import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.Mockito.when;

@WebMvcTest(RecommendationController.class)
@ExtendWith(MockitoExtension.class)
public class TestRecommendationControllerMvc {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecommendationController recommendationController;

    @Test
    public void testGetRecommendation_Ok_EmptyList() throws Exception {
        UUID userId = UUID.randomUUID();
        Recommendation recommendation = new Recommendation(userId, new ArrayList<>());
        when(recommendationController.getRecommendation(userId)).thenReturn(recommendation);

        mockMvc.perform(MockMvcRequestBuilders.get("/recommendation/{userId}", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user_id").value(userId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recommendations").isEmpty());
    }
}

