package ru.star.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.star.bank.entity.RuleStatsEntity;

public interface RuleStatsRepository extends JpaRepository<RuleStatsEntity, String> {
}
