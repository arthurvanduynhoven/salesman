package com.example.salesman;

import com.example.salesman.model.Product;
import com.example.salesman.model.ProductScore;
import com.example.salesman.model.Rule;
import com.example.salesman.service.ProductRuleScoreService;
import com.example.salesman.service.ProductScoreService;
import com.example.salesman.service.ProductService;
import com.example.salesman.service.RuleService;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.UUID;

@ShellComponent
public class Commands {
    @Autowired
    private ProductService productService;
    @Autowired
    private RuleService ruleService;
    @Autowired
    private ProductRuleScoreService productRuleScoreService;
    @Autowired
    private ProductScoreService productScoreService;

    @ShellMethod(
            value = "Auto generate products",
            key = "generate",
            group = "Product Commands"
    )
    public void Generate(
            @ShellOption(defaultValue = "100") Integer quantity
    ) {
        Faker faker = new Faker();
        for (int i = 0; i < quantity; i++) {
            Product product = productService.create(
                    Product.builder()
                            .productRef(UUID.randomUUID().toString())
                            .name(faker.superhero().name())
                            .color(faker.color().name())
                            .cost(faker.number().randomDouble(2, 0, 1000))
                            .quantity(faker.number().numberBetween(0, 10000))
                            .taxExempt(faker.bool().bool())
                            .build()
            );

            ProductScore productScore = ProductScore.builder()
                    .product(product)
                    .id(product.getId())
                    .build();

            // process each rule for product
            ruleService.list().forEach(rule -> productRuleScoreService.calculate(rule, product));
            // recalculate product total score
            System.out.println(productScoreService.calculate(productScore));
        }
    }

    @ShellMethod(
            value = "Retrieve all available products",
            key = "products",
            group = "Product Commands"
    )
    public void Products() {
        productService.list().forEach(System.out::println);
    }

    @ShellMethod(
            value = "Retrieve products to purchase based on threshold",
            key = "purchases",
            group = "Product Commands"
    )
    public void Purchases(
            @ShellOption(defaultValue = "50") Double threshold
    ) {
        productService.listByThreshold(threshold).forEach(System.out::println);
    }

    @ShellMethod(
            value = "Retrieve summary products to purchase based on threshold",
            key = "summary",
            group = "Product Commands"
    )
    public void Summary(
            @ShellOption(defaultValue = "1.0") Double threshold
    ) {
        productService.summaryByThreshold(threshold).forEach(System.out::println);
    }

    @ShellMethod(
            value = "Retrieve defined rules",
            key = "rules",
            group = "Rule Commands"
    )
    public void Rules() {
        ruleService.list().forEach(System.out::println);
    }

    @ShellMethod(
            value = "Create new rule",
            key = "rule",
            group = "Rule Commands"
    )
    public void Rule(
            String rule,
            Double weight
    ) {
        Rule ruleEntity = ruleService.create(Rule.builder().rule(rule).weight(weight).build());
        productService.list().forEach(product -> {
            productRuleScoreService.calculate(ruleEntity, product);
            productScoreService.calculate(product.getProductScore());
        });
    }

    @ShellMethod(
            value = "List Product Rule Scores",
            key = "rule-scores",
            group = "Score Commands"
    )
    public void ProductRuleScores() {
        productRuleScoreService.list().forEach(System.out::println);
    }

    @ShellMethod(
            value = "List Product Scores",
            key = "product-scores",
            group = "Score Commands"
    )
    public void ProductScores() {
        productScoreService.list().forEach(System.out::println);
    }
}
