package pro.sky.star_bank.recommendation.service;

import org.springframework.stereotype.Service;
import pro.sky.star_bank.recommendation.model.RecommendedProduct;
import pro.sky.star_bank.recommendation.model.RecommendedProductFix1;
import pro.sky.star_bank.recommendation.model.RecommendedProductFix2;
import pro.sky.star_bank.recommendation.model.RecommendedProductFix3;
import pro.sky.star_bank.recommendation.repository.RecommendedProductRepository;
import pro.sky.star_bank.recommendation.repository.TransactionsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendedProductRepository recommendedProductRepository;

    private final TransactionsRepository transactionsRepository;

    private final List<RecommendedProduct> recommendationsFix = new ArrayList<>();

    public RecommendationServiceImpl(RecommendedProductRepository recommendedProductRepository, TransactionsRepository transactionsRepository) {
        this.recommendedProductRepository = recommendedProductRepository;
        this.transactionsRepository = transactionsRepository;

        this.recommendationsFix.add(new RecommendedProductFix1(transactionsRepository));
        this.recommendationsFix.add(new RecommendedProductFix2(transactionsRepository));
        this.recommendationsFix.add(new RecommendedProductFix3(transactionsRepository));

    }

    @Override
    public List<RecommendedProduct> getRecommendations(UUID userId) {
        return recommendationsFix.stream()
                .filter(r -> r.checkForUser(userId))
                .toList();
    }
}
