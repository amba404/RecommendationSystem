package pro.sky.star.bank.recommendation.repository;


import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.star.bank.recommendation.model.RuleStat;

import java.util.List;
import java.util.UUID;

public interface RuleStatRepository extends JpaRepository<RuleStat, UUID> {

    @NotNull
    @Query(value = """
            select r.id as rule_id, coalesce(rs.rule_count, 0) as rule_count
            from rule_set r left join rule_stat rs on r.id = rs.rule_id;
            """, nativeQuery = true)
    List<RuleStat> findAll();

    @Modifying
    @Transactional
    @Query(value = """
            merge into rule_stat as t using rule_set as s on t.rule_id = s.id and s.id = ?1
            when matched then update set rule_count = rule_count + 1
            when not matched then insert (rule_id, rule_count) values (s.id, 1);
            """, nativeQuery = true)
    void incrementCount(UUID id);
}
