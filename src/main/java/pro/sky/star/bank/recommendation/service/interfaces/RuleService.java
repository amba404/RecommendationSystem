package pro.sky.star.bank.recommendation.service.interfaces;

import pro.sky.star.bank.recommendation.model.RuleSet;

import java.util.List;
import java.util.UUID;

public interface RuleService {

    RuleSet addRuleSet(RuleSet ruleSet);

    List<RuleSet> findAll();

    void deleteRuleSet(UUID id);

    boolean checkForUser(UUID userId, RuleSet ruleSet);

}
