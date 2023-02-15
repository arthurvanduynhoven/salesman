package com.example.salesman.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRuleScore {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Double score;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rule_id")
    private Rule rule;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @Override
    public String toString() {
        return "ProductRuleScore{" +
                "score=" + score +
                ", ruleId=" + rule.getId() +
                ", productId=" + product.getId() +
                '}';
    }
}
