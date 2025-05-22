package pro.sky.star_bank.recommendation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.star_bank.recommendation.model.Recommendation;
import pro.sky.star_bank.recommendation.service.RecommendationService;

import java.util.UUID;

/**
 * Контроллер сервиса рекомендаций. Выдает рекомендованные банковские продукты для заданного пользователя
 */
@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{userId}")
    public Recommendation getRecommendation(@PathVariable(name = "userId") UUID userId) {
        return new Recommendation(userId, recommendationService.getRecommendations(userId));
    }
}
