package pro.sky.star_bank.recommendation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.star_bank.recommendation.model.RuleStat;
import pro.sky.star_bank.recommendation.repository.RuleStatRepository;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для работы со статистикой по динамическим правилам
 */
@Service
@RequiredArgsConstructor
public class RuleStatServiceImpl implements RuleStatService {

    private final RuleStatRepository ruleStatRepository;

    /**
     * Получить ВСЕ правила и статистику их успешного срабатывания
     *
     * @return List
     */
    @Override
    public List<RuleStat> findAll() {
        return ruleStatRepository.findAll();
    }

    /**
     * Увеличить счетчик успешного срабатывания правила на 1
     *
     * @param id
     */
    @Override
    public void incrementCount(UUID id) {
        ruleStatRepository.incrementCount(id);
    }
}
