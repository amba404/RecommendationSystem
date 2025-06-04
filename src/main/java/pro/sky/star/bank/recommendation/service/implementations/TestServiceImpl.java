package pro.sky.star.bank.recommendation.service.implementations;

import org.springframework.stereotype.Service;
import pro.sky.star.bank.recommendation.repository.TransactionsRepository;
import pro.sky.star.bank.recommendation.service.interfaces.TestService;

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
     * @param userId идентификатор пользователя
     * @return Integer
     */
    @Override
    public Integer getAmount(UUID userId) {
        return repository.getRandomTransactionAmount(userId);
    }
}
