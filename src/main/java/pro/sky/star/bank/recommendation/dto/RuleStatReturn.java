package pro.sky.star.bank.recommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pro.sky.star.bank.recommendation.model.RuleStat;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RuleStatReturn {
    private List<RuleStat> stats;
}
