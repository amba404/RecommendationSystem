package pro.sky.star.bank.recommendation.service.interfaces;

import pro.sky.star.bank.recommendation.model.RecommendedProduct;

import java.util.List;
import java.util.UUID;

public interface RecommendationService {
    List<RecommendedProduct> getRecommendations(UUID userId);
}
