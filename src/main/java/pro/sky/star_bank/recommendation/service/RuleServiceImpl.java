package pro.sky.star_bank.recommendation.service;

import org.springframework.stereotype.Service;
import pro.sky.star_bank.recommendation.model.RuleSet;
import pro.sky.star_bank.recommendation.repository.RecommendedProductRepository;
import pro.sky.star_bank.recommendation.repository.RuleSetRepository;

import java.util.List;
import java.util.UUID;

@Service
public class RuleServiceImpl implements RuleService {

    private final RuleSetRepository rulesetRepository;

    private final RecommendedProductRepository productRepository;

    public RuleServiceImpl(RuleSetRepository rulesetRepository, RecommendedProductRepository productRepository) {
        this.rulesetRepository = rulesetRepository;
        this.productRepository = productRepository;
    }

    @Override
    public RuleSet addRuleSet(RuleSet ruleSet) {
        ruleSet.assertValid();

        productRepository.save(ruleSet.getProduct());

        return rulesetRepository.save(ruleSet);
    }

    @Override
    public List<RuleSet> getAll() {
        return rulesetRepository.findAll();
    }

    @Override
    public void deleteRuleSet(UUID id) {
        rulesetRepository.deleteById(id);
    }
}
