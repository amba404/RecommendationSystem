package pro.sky.star_bank.recommendation.service;

import java.util.UUID;

public interface RecommendationRuleSet {
    boolean checkForUser(UUID userId);
}
