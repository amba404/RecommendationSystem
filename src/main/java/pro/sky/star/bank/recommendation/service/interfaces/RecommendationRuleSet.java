package pro.sky.star.bank.recommendation.service.interfaces;

import java.util.UUID;

public interface RecommendationRuleSet {
    boolean checkForUser(UUID userId);
}
