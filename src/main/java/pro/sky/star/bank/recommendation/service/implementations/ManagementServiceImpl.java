package pro.sky.star.bank.recommendation.service.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import pro.sky.star.bank.recommendation.service.interfaces.ManagementService;

import java.util.Objects;

/**
 * Сервис служебных операций
 */
@Service
@EnableCaching
@RequiredArgsConstructor
public class ManagementServiceImpl implements ManagementService {

    private final CacheManager cacheManager;

    /**
     * Очистка кэшей запросов к БД транзакций
     */
    @Override
    public void clearCaches() {
        cacheManager.getCacheNames()
                .forEach(name ->
                        Objects.requireNonNull(cacheManager.getCache(name))
                                .clear());
    }
}

