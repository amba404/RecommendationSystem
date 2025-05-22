package pro.sky.star_bank.recommendation.service;

import org.springframework.stereotype.Service;
import pro.sky.star_bank.recommendation.repository.TransactionsRepository;

import java.util.UUID;

/**
 * Простой сервис для тестирования корректности подключения БД транзакций
 */
@Service
public class TestServiceImpl implements TestService {

    private final TransactionsRepository repository;

    public TestServiceImpl(TransactionsRepository repository) {
        this.repository = repository;
    }


    /**
     * Получить сумму случайной транзакции для указанного пользователя
     *
     * @param userId
     * @return Integer
     */
    @Override
    public Integer getAmount(UUID userId) {
        return repository.getRandomTransactionAmount(userId);
    }
}
