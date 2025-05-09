package pro.sky.star_bank.recommendation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.star_bank.recommendation.model.RuleSet;

import java.util.UUID;

public interface RuleSetRepository extends JpaRepository<RuleSet, UUID> {
}
