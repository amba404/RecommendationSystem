package pro.sky.star_bank.recommendation.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;
import pro.sky.star_bank.recommendation.service.RecommendationRuleSet;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "rule_set")
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RuleSet implements RecommendationRuleSet {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @OneToOne
    @JsonUnwrapped(prefix = "product_")
    private RecommendedProduct product;

    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "rule", columnDefinition = "jsonb")
    @JsonProperty("rule")
    private List<Rule> rules;

    public RuleSet() {
        this.id = UUID.randomUUID();
    }

    @Override
    public boolean checkForUser(UUID userId) {
        return false;
    }
}
