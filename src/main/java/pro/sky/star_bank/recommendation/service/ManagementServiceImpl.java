package pro.sky.star_bank.recommendation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

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

