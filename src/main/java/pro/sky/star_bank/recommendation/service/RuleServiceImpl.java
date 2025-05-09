package pro.sky.star_bank.recommendation.service;

import org.springframework.stereotype.Service;
import pro.sky.star_bank.recommendation.model.RuleSet;
import pro.sky.star_bank.recommendation.repository.RecommendedProductRepository;
import pro.sky.star_bank.recommendation.repository.RuleSetRepository;
import pro.sky.star_bank.recommendation.repository.TransactionsRepository;

import java.util.List;
import java.util.UUID;

@Service
public class RuleServiceImpl implements RuleService {

    private final RuleSetRepository rulesetRepository;

    private final TransactionsRepository transactionsRepository;

    private final RecommendedProductRepository productRepository;

    public RuleServiceImpl(RuleSetRepository rulesetRepository, TransactionsRepository transactionsRepository, RecommendedProductRepository productRepository) {
        this.rulesetRepository = rulesetRepository;
        this.transactionsRepository = transactionsRepository;
        this.productRepository = productRepository;
    }

    @Override
    public boolean checkForUser(UUID userId, RuleSet ruleSet) {
        return transactionsRepository.checkForUser(userId, ruleSet);
    }

    @Override
    public RuleSet addRuleSet(RuleSet ruleSet) {
        ruleSet.assertValid();

        productRepository.save(ruleSet.getProduct());

        return rulesetRepository.save(ruleSet);
    }

    @Override
    public List<RuleSet> findAll() {
        return rulesetRepository.findAll();
    }

    @Override
    public void deleteRuleSet(UUID id) {
        rulesetRepository.deleteById(id);
    }
}
