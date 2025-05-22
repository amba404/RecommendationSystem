package pro.sky.star_bank.recommendation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import pro.sky.star_bank.recommendation.service.RecommendationRuleSet;

import java.util.UUID;

/**
 * Модель данных для рекомендуемого продукта, с проверкой применимости рекомендации к заданному пользователю
 */
@Entity
@Table(name = "recommended_products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RecommendedProduct implements RecommendationRuleSet {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String text;

    @Override
    public boolean checkForUser(UUID userId) {
        return false;
    }
}
