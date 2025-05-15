package pro.sky.star_bank.recommendation.service;

import pro.sky.star_bank.recommendation.model.RuleStat;

import java.util.List;
import java.util.UUID;

public interface RuleStatService {

    List<RuleStat> findAll();

    void incrementCount(UUID id);

}
