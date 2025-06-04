package pro.sky.star.bank.recommendation.service.implementations;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import pro.sky.star.bank.recommendation.model.Rule;
import pro.sky.star.bank.recommendation.model.RuleSet;
import pro.sky.star.bank.recommendation.repository.RecommendedProductRepository;
import pro.sky.star.bank.recommendation.repository.RuleSetRepository;
import pro.sky.star.bank.recommendation.repository.TransactionsRepository;
import pro.sky.star.bank.recommendation.service.interfaces.RuleService;
import pro.sky.star.bank.recommendation.service.interfaces.RuleStatService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для работы с динамическими правилами рекомендации
 */
@Service
@RequiredArgsConstructor
public class RuleServiceImpl implements RuleService {

    private final RuleSetRepository rulesetRepository;
    private final TransactionsRepository transactionsRepository;
    private final RecommendedProductRepository productRepository;
    private final RuleStatService ruleStatService;

    /**
     * Проверяет выполнение Набора правил рекомендации пользователя
     *
     * @param userId  идентификатор пользователя
     * @param ruleSet набор правил рекомендации
     * @return True | false
     */
    @Override
    public boolean checkForUser(@NotNull UUID userId, @NotNull RuleSet ruleSet) {
        List<Rule> rules = ruleSet.getRules();
        boolean result = !rules.isEmpty();

        for (Rule rule : rules) {
            result = result && checkForUser(userId, rule);
            if (!result) {
                return false;
            }
        }

        ruleStatService.incrementCount(ruleSet.getId());

        return true;
    }

    /**
     * Проверяет выполнение правила рекомендации для пользователя
     *
     * @param userId идентификатор пользователя
     * @param rule   правило рекомендации
     * @return True | false
     */
    public boolean checkForUser(@NotNull UUID userId, @NotNull Rule rule) {
        Optional<Boolean> result = switch (rule.getQueryType()) {
            case USER_OF -> transactionsRepository.checkRuleUserOf(userId, rule.getProductType(), Rule.CNT_USER_OF);
            case ACTIVE_USER_OF ->
                    transactionsRepository.checkRuleUserOf(userId, rule.getProductType(), Rule.CNT_ACTIVE_USER_OF);
            case TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW ->
                    transactionsRepository.checkRuleTransactionSumCompareDepositWithdraw(userId, rule.getProductType(), rule.getCompareType());
            case TRANSACTION_SUM_COMPARE ->
                    transactionsRepository.checkRuleTransactionSumCompare(userId, rule.getProductType(), rule.getTransactionType(), rule.getCompareType(), rule.getCompareValue());
        };
        return rule.isNegate() ^ result.orElse(false);
    }


    /**
     * Добавляет набор правил рекомендации. Проверяет добавляемый набор на валидность. Добавляет/обновляет информацию по рекомендуемому продукту.
     *
     * @param ruleSet
     * @return ruleSet
     */
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
