package pro.sky.star_bank.recommendation.service;

import com.github.benmanes.caffeine.cache.Cache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import pro.sky.star_bank.recommendation.model.enums.EnumProductType;
import pro.sky.star_bank.recommendation.repository.TransactionsRepository;

import java.util.UUID;

@SpringBootTest
class ManagementServiceImplTest {

    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private ManagementService managementService;
    @Autowired
    private TransactionsRepository transactionsRepository;

    @Test
    void clearCaches() {
        Boolean result = transactionsRepository.checkRuleUserOf(UUID.randomUUID(), EnumProductType.DEBIT, 1).orElse(false);
        long sizeBefore = getCachesSize();

        managementService.clearCaches();

        Long sizeAfter = getCachesSize();

        Assertions.assertTrue(sizeBefore > 0);
        Assertions.assertEquals(0, sizeAfter);
    }

    private long getCachesSize() {
        return cacheManager.getCacheNames().stream()
                .mapToLong(name -> ((Cache<Object, Object>) cacheManager.getCache(name).getNativeCache())
                        .estimatedSize()
                ).sum();
    }
}