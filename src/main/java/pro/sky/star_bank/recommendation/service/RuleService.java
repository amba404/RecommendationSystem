package pro.sky.star_bank.recommendation.service;

import pro.sky.star_bank.recommendation.model.RuleSet;

import java.util.List;
import java.util.UUID;

public interface RuleService {

    RuleSet addRuleSet(RuleSet ruleSet);

    List<RuleSet> findAll();

    void deleteRuleSet(UUID id);

    boolean checkForUser(UUID userId, RuleSet ruleSet);

}
