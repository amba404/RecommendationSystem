package pro.sky.star.bank.recommendation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.star.bank.recommendation.model.Recommendation;
import pro.sky.star.bank.recommendation.service.interfaces.RecommendationService;

import java.util.UUID;

/**
 * Контроллер сервиса рекомендаций. Выдает рекомендованные банковские продукты для заданного пользователя
 */
@RestController
@RequestMapping("/recommendation")
@Tag(name = "Recommendation Controller", description = "Контроллер сервиса рекомендаций. Выдает рекомендованные банковские продукты для заданного пользователя")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Выдача рекомендаций", description = "Список рекомендованных продуктов для заданного пользователя")
    public Recommendation getRecommendation(@PathVariable(name = "userId") UUID userId) {
        return new Recommendation(userId, recommendationService.getRecommendations(userId));
    }
}
