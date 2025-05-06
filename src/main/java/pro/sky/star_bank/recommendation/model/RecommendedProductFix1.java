package pro.sky.star_bank.recommendation.model;

import org.springframework.stereotype.Component;
import pro.sky.star_bank.recommendation.repository.TransactionsRepository;
import pro.sky.star_bank.recommendation.service.RecommendationRuleSet;

import java.util.UUID;

@Component
public class RecommendedProductFix1 extends RecommendedProduct implements RecommendationRuleSet {

    private final TransactionsRepository transactionsRepository;

    public RecommendedProductFix1(TransactionsRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
        this.setId(UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a"));
        this.setName("Invest 500");
        this.setText("Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до конца года и получите выгоду в виде вычета на взнос в следующем налоговом периоде. Не упустите возможность разнообразить свой портфель, снизить риски и следить за актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!");
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
                SELECT count(*) = 0
                FROM
                    transactions t
                        INNER JOIN PRODUCTS p ON
                        t.PRODUCT_ID = p.ID
                WHERE
                    t.user_id = ? and p.TYPE = 'INVEST'
                """;

        String sql3 = """
                SELECT sum(t.AMOUNT) > 1000
                FROM
                    transactions t
                        INNER JOIN PRODUCTS p ON
                        t.PRODUCT_ID = p.ID
                WHERE
                    t.user_id = ? and p.TYPE = 'SAVING' and t.TYPE = 'DEPOSIT'
                """;

        return transactionsRepository.checkTextRule(sql1, userId).orElse(false)
                &&
                transactionsRepository.checkTextRule(sql2, userId).orElse(false)
                &&
                transactionsRepository.checkTextRule(sql3, userId).orElse(false);
    }
}
