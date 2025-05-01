package pro.sky.star_bank.recommendation.service;

import org.springframework.stereotype.Service;
import pro.sky.star_bank.recommendation.model.RecommendedProduct;
import pro.sky.star_bank.recommendation.repository.RecommendedProductRepository;

import java.util.List;
import java.util.UUID;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendedProductRepository recommendedProductRepository;

    public RecommendationServiceImpl(RecommendedProductRepository recommendedProductRepository) {
        this.recommendedProductRepository = recommendedProductRepository;
    }

    @Override
    public List<RecommendedProduct> getRecommendations(UUID userId) {
        return recommendedProductRepository.findAll();
    }
}
