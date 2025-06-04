package pro.sky.star.bank.recommendation.service.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.star.bank.recommendation.model.RuleStat;
import pro.sky.star.bank.recommendation.repository.RuleStatRepository;
import pro.sky.star.bank.recommendation.service.interfaces.RuleStatService;

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
     * @param id идентификатор правила
     */
    @Override
    public void incrementCount(UUID id) {
        ruleStatRepository.incrementCount(id);
    }
}
