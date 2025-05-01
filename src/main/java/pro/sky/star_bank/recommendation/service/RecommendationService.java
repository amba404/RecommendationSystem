package pro.sky.star_bank.recommendation.service;

import pro.sky.star_bank.recommendation.model.RecommendedProduct;

import java.util.List;
import java.util.UUID;

public interface RecommendationService {
    List<RecommendedProduct> getRecommendations(UUID userId);
}
