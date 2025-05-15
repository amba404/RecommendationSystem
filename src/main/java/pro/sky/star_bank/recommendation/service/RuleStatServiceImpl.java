package pro.sky.star_bank.recommendation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.star_bank.recommendation.model.RuleStat;
import pro.sky.star_bank.recommendation.repository.RuleStatRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RuleStatServiceImpl implements RuleStatService{

    private final RuleStatRepository ruleStatRepository;

    @Override
    public List<RuleStat> findAll() {
        return ruleStatRepository.findAll();
    }

    @Override
    public void incrementCount(UUID id) {
        ruleStatRepository.incrementCount(id);
    }
}
