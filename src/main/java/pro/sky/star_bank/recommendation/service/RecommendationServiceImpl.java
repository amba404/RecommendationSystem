package pro.sky.star_bank.recommendation.service;

import org.springframework.stereotype.Service;
import pro.sky.star_bank.recommendation.model.RecommendedProduct;
import pro.sky.star_bank.recommendation.repository.RecommendedProductRepository;
import pro.sky.star_bank.recommendation.repository.TransactionsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendedProductRepository recommendedProductRepository;

    private final TransactionsRepository transactionsRepository;

    public RecommendationServiceImpl(RecommendedProductRepository recommendedProductRepository, TransactionsRepository transactionsRepository) {
        this.recommendedProductRepository = recommendedProductRepository;
        this.transactionsRepository = transactionsRepository;
    }

    @Override
    public List<RecommendedProduct> getRecommendations(UUID userId) {
        List<RecommendedProduct> recommendations = new ArrayList<>();
        List<UUID> uuids = transactionsRepository.getRecommendations(userId);

        for (UUID uuid : uuids) {
            recommendedProductRepository.findById(uuid).ifPresent(recommendations::add);
        }
        return recommendations;
    }
}
