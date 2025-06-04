package pro.sky.star.bank.recommendation.service.implementations;

import org.springframework.stereotype.Service;
import pro.sky.star.bank.recommendation.model.RecommendedProduct;
import pro.sky.star.bank.recommendation.service.interfaces.RecommendationRuleSet;
import pro.sky.star.bank.recommendation.service.interfaces.RecommendationService;
import pro.sky.star.bank.recommendation.service.interfaces.RuleService;

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
     * @param userId Идентификатор пользователя
     * @return List<RecommendedProduct>
     */
    @Override
    public List<RecommendedProduct> getRecommendations(UUID userId) {

        HashSet<RecommendedProduct> recommendations = new HashSet<>();

        ruleService.findAll().stream()
                .parallel()
                .filter(r -> ruleService.checkForUser(userId, r))
                .forEach(r -> recommendations.add(r.getProduct()));

        recommendationsFix.stream()
                .filter(r -> (!recommendations.contains((RecommendedProduct) r)))
                .filter(r -> r.checkForUser(userId))
                .forEach(r -> recommendations.add((RecommendedProduct) r));

        return List.copyOf(recommendations);
    }
}
