package pro.sky.star.bank.recommendation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Класс для хранения статистики успешного применения правил для выдачи рекомендаций
 */
@Entity
@Table(name = "rule_stat")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Schema(description = "Статистика успешного применения правил для выдачи рекомендаций")
public class RuleStat {

    @EqualsAndHashCode.Include
    @Id
    @JsonProperty("rule_id")
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JsonIgnore
    private RuleSet rule;

    @Column(name = "rule_count")
    @JsonProperty("count")
    private int rulesetCount;

}
