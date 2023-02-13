package com.example.salesman.utils;

import com.example.salesman.model.Product;
import org.springframework.expression.Expression;

import java.util.List;

public class ScoreUtils {

    public static Double calculate(final Product product,
                                   final Double weight,
                                   final List<Expression> expressions) {
        double matched = expressions.stream().map(expression -> expression.getValue(product)).
                filter(Boolean.TRUE::equals).count();
        return matched / expressions.size() * weight;
    }
}
