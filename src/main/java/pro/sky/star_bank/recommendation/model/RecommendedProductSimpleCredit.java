package pro.sky.star_bank.recommendation.model;

import org.springframework.stereotype.Component;
import pro.sky.star_bank.recommendation.repository.TransactionsRepository;
import pro.sky.star_bank.recommendation.service.RecommendationRuleSet;

import java.util.UUID;

/**
 * Рекомендованный продукт с фиксированным правилом рекомендации "Простой кредит"
 * <p>
 * Устарело. Может быть заменено динамическими правилами рекомендаций
 */

@Component
public class RecommendedProductSimpleCredit extends RecommendedProduct implements RecommendationRuleSet {

    private final TransactionsRepository transactionsRepository;

    public RecommendedProductSimpleCredit(TransactionsRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
        this.setId(UUID.fromString("ab138afb-f3ba-4a93-b74f-0fcee86d447f"));
        this.setName("Простой кредит");
        this.setText("Откройте мир выгодных кредитов с нами!" +
                "Ищете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный кредит — именно то, что вам нужно! Мы предлагаем низкие процентные ставки, гибкие условия и индивидуальный подход к каждому клиенту." +
                "Почему выбирают нас:" +
                "Быстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения заявки занимает всего несколько часов." +
                "Удобное оформление. Подать заявку на кредит можно онлайн на нашем сайте или в мобильном приложении." +
                "Широкий выбор кредитных продуктов. Мы предлагаем кредиты на различные цели: покупку недвижимости, автомобиля, образование, лечение и многое другое." +
                "Не упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!");
    }

    @Override
    public boolean checkForUser(UUID userId) {

        return transactionsRepository.checkForUserSimpleCredit(userId);
    }
}
