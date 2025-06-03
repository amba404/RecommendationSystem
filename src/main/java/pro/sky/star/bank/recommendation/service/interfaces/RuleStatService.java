package pro.sky.star.bank.recommendation.service.interfaces;

import pro.sky.star.bank.recommendation.model.RuleStat;

import java.util.List;
import java.util.UUID;

public interface RuleStatService {

    List<RuleStat> findAll();

    void incrementCount(UUID id);

}
