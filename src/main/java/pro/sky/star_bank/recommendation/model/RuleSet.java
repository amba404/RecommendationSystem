package pro.sky.star_bank.recommendation.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "rule_set")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RuleSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private UUID id;

    @OneToOne
    @JsonUnwrapped(prefix = "product_")
    @NotNull
    private RecommendedProduct product;

    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "rule", columnDefinition = "jsonb")
    @JsonProperty("rule")
    @NotNull
    private List<Rule> rules;

    public void assertValid() {
        if (rules == null) {
            throw new IllegalArgumentException("Rules can't be null");
        }
        if (rules.isEmpty()) {
            throw new IllegalArgumentException("Rules can't be empty");
        }
        rules.forEach(Rule::assertValid);
    }
}
