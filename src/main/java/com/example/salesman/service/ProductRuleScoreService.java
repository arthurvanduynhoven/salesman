package com.example.salesman.service;

import com.example.salesman.model.Product;
import com.example.salesman.model.ProductRuleScore;
import com.example.salesman.model.Rule;
import com.example.salesman.repository.ProductRuleScoreRepository;
import com.example.salesman.utils.ScoreUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductRuleScoreService {

    private final ProductRuleScoreRepository productRuleScoreRepository;

    public ProductRuleScore calculate(Rule rule, Product product) {
        ProductRuleScore productRuleScore = productRuleScoreRepository.findByRuleIdAndProductId(
                        rule.getId(), product.getId())
                .orElse(ProductRuleScore.builder()
                        .rule(rule)
                        .product(product)
                        .build()
                );
        productRuleScore.setScore(ScoreUtils.calculate(
                productRuleScore.getProduct(),
                productRuleScore.getRule().getWeight(),
                productRuleScore.getRule().getExpressions())
        );
        return productRuleScoreRepository.save(productRuleScore);
    }

    public Iterable<ProductRuleScore> list() {
        return productRuleScoreRepository.findAll();
    }
}
