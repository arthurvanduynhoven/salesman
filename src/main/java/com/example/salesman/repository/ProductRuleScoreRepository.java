package com.example.salesman.repository;

import com.example.salesman.model.ProductRuleScore;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ProductRuleScoreRepository extends CrudRepository<ProductRuleScore, Long> {
    Optional<ProductRuleScore> findByRuleIdAndProductId(Long ruleId, Long productId);

    Collection<ProductRuleScore> findAllByProductId(Long productId);
}
