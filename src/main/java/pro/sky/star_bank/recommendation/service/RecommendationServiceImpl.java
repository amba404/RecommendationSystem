package pro.sky.star_bank.recommendation.service;

import org.springframework.stereotype.Service;
import pro.sky.star_bank.recommendation.model.RecommendedProduct;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * Сервис для получения рекомендаций банковских продуктов.
 * Основан на наборах динамических правил.
 * Сохранена выдача фиксированных правил рекомендаций
 */
@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final RuleService ruleService;

    private final List<RecommendationRuleSet> recommendationsFix;

    public RecommendationServiceImpl(RuleService ruleService, List<RecommendationRuleSet> recommendationsFix) {
        this.ruleService = ruleService;
        this.recommendationsFix = recommendationsFix;
    }

    /**
     * Получить рекомендации для пользователя
     * @param userId
     * @return List<RecommendedProduct>
     */
    @Override
    public List<RecommendedProduct> getRecommendations(UUID userId) {

        HashSet<RecommendedProduct> recommendations = new HashSet<>();

        recommendationsFix.stream()
                .parallel()
                .filter(r -> r.checkForUser(userId))
                .forEach(r -> recommendations.add((RecommendedProduct) r));

        ruleService.findAll().stream()
                .parallel()
                .filter(r -> ruleService.checkForUser(userId, r))
                .forEach(r -> recommendations.add(r.getProduct()));

        return List.copyOf(recommendations);
    }
}
