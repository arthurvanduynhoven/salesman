package com.example.salesman.service;

import com.example.salesman.model.ProductRuleScore;
import com.example.salesman.model.ProductScore;
import com.example.salesman.repository.ProductRuleScoreRepository;
import com.example.salesman.repository.ProductScoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

@Service
@AllArgsConstructor
public class ProductScoreService {
    private final ProductScoreRepository productScoreRepository;

    private final ProductRuleScoreRepository productRuleScoreRepository;

    public ProductScore calculate(ProductScore productScore) {
        Collection<ProductRuleScore> productRuleScoreList = productRuleScoreRepository.findAllByProductId(
                productScore.getId());
        productScore.setScore(
                CollectionUtils.isEmpty(productRuleScoreList) ? 0d :
                        productRuleScoreList.stream().mapToDouble(ProductRuleScore::getScore).sum()
        );
        return productScoreRepository.save(productScore);
    }

    public Iterable<ProductScore> list() {
        return productScoreRepository.findAll();
    }
}
