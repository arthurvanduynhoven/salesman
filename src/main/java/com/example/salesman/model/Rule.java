package com.example.salesman.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String rule;
    private Double weight;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "rule", orphanRemoval = true, cascade = CascadeType.ALL)
    private Collection<ProductRuleScore> productRuleScores = new ArrayList<>();

    public List<Expression> getExpressions() {
        return Arrays.stream(rule.split("&&")).map(
                r -> new SpelExpressionParser().parseExpression(r)
        ).toList();
    }

    @Override
    public String toString() {
        return "Rule{" +
                "id=" + id +
                ", rule='" + rule + '\'' +
                ", weight=" + weight +
                '}';
    }
}
