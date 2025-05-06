package pro.sky.star_bank.recommendation.model;

import org.springframework.stereotype.Component;
import pro.sky.star_bank.recommendation.repository.TransactionsRepository;
import pro.sky.star_bank.recommendation.service.RecommendationRuleSet;

import java.util.UUID;

@Component
public class RecommendedProductFix2 extends RecommendedProduct implements RecommendationRuleSet {

    private final TransactionsRepository transactionsRepository;

    public RecommendedProductFix2(TransactionsRepository transactionsRepository) {
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

        String sql1 = """
                SELECT count(*) > 0
                FROM
                    transactions t
                        INNER JOIN PRODUCTS p ON
                        t.PRODUCT_ID = p.ID
                WHERE
                    t.user_id = ? and p.TYPE = 'DEBIT'
                """;

        String sql2 = """
                SELECT sum(t.AMOUNT) >= 50000
                FROM
                    transactions t
                        INNER JOIN PRODUCTS p ON
                        t.PRODUCT_ID = p.ID
                WHERE
                    t.user_id = ? and p.TYPE = 'DEBIT' and t.TYPE = 'DEPOSIT'
                """;

        String sql3 = """
                SELECT sum(t.AMOUNT) >= 50000
                FROM
                    transactions t
                        INNER JOIN PRODUCTS p ON
                        t.PRODUCT_ID = p.ID
                WHERE
                    t.user_id = ? and p.TYPE = 'SAVING' and t.TYPE = 'DEPOSIT'
                """;


        String sql4 = """
                SELECT SUM(CASE WHEN t.TYPE = 'DEPOSIT' THEN amount ELSE 0 END) >
                		SUM(CASE WHEN t.TYPE = 'WITHDRAW' THEN amount ELSE 0 END)
                FROM
                    transactions t
                        INNER JOIN PRODUCTS p ON
                        t.PRODUCT_ID = p.ID
                WHERE
                    t.user_id = ? and p.TYPE = 'DEBIT'
                """;

        return transactionsRepository.checkTextRule(sql1, userId).orElse(false)
                &&
                (
                        transactionsRepository.checkTextRule(sql2, userId).orElse(false)
                                ||
                                transactionsRepository.checkTextRule(sql3, userId).orElse(false)
                )
                &&
                transactionsRepository.checkTextRule(sql4, userId).orElse(false);
    }
}
