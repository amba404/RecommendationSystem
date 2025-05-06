package pro.sky.star_bank.recommendation.service;

import java.util.UUID;

public interface RecommendationRuleSetFix {
    boolean checkForUser(UUID userId);
}
