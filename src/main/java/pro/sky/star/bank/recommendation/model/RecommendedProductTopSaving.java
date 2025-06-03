package pro.sky.star.bank.recommendation.model;

import org.springframework.stereotype.Component;
import pro.sky.star.bank.recommendation.repository.TransactionsRepository;
import pro.sky.star.bank.recommendation.service.interfaces.RecommendationRuleSet;

import java.util.UUID;

/**
 * Рекомендованный продукт с фиксированным правилом рекомендации "Top Saving"
 * <p>
 * Устарело. Может быть заменено динамическими правилами рекомендаций
 */
@Component
public class RecommendedProductTopSaving extends RecommendedProduct implements RecommendationRuleSet {

    private final TransactionsRepository transactionsRepository;

    public RecommendedProductTopSaving(TransactionsRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
        this.setId(UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925"));
        this.setName("Top Saving");
        this.setText("Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. Больше никаких забытых чеков и потерянных квитанций — всё под контролем!\n" +
                "Преимущества «Копилки»:\n" +
                "Накопление средств на конкретные цели. Установите лимит и срок накопления, и банк будет автоматически переводить определенную сумму на ваш счет.\n" +
                "Прозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте стратегию при необходимости.\n" +
                "Безопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только через мобильное приложение или интернет-банкинг.\n" +
                "Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!");
    }

    @Override
    public boolean checkForUser(UUID userId) {

        return transactionsRepository.checkForUserTopSaving(userId);
    }

}
