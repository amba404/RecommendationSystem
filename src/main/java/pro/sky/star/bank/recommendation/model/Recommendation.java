package pro.sky.star.bank.recommendation.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * Модель данных для возврата рекомендаций сервисом рекомендаций
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Recommendation {

    @EqualsAndHashCode.Include
    @JsonProperty("user_id")
    private UUID userId;

    @JsonProperty("recommendations")
    private List<RecommendedProduct> recommendedProducts;
}
