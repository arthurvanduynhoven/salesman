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
            @ShellOption(defaultValue = "100") Integer quantity,
            @ShellOption(defaultValue = "500") Long maxCost,
            @ShellOption(defaultValue = "1000") Integer maxQuantity
    ) {
        Faker faker = new Faker();
        for (int i = 0; i < quantity; i++) {
            // Create some test product data
            Product product = productService.create(
                    Product.builder()
                            .productRef(UUID.randomUUID().toString())
                            .name(faker.superhero().name())
                            .color(faker.color().name())
                            .price(faker.number().randomDouble(2, 0, maxCost))
                            .quantity(faker.number().numberBetween(0, maxQuantity))
                            .taxExempt(faker.bool().bool())
                            .build()
            );

            // Create overall product score -- logic should be moved to database side for auto-creation
            ProductScore productScore = ProductScore.builder()
                    .product(product)
                    .id(product.getId())
                    .build();

            // process each rule for product - could move to a db trigger
            ruleService.list().forEach(rule -> productRuleScoreService.calculate(rule, product));
            // recalculate product total score - could move to a db trigger
            System.out.println(productScoreService.calculate(productScore));
        }
    }

    @ShellMethod(
            value = "Retrieve all available products",
            key = "products",
            group = "Product Commands"
    )
    public void Products() {
        // Add pagination query args
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
            value = "Retrieve summary of products to purchase based on threshold",
            key = "summary",
            group = "Product Commands"
    )
    public void Summary(
            @ShellOption(defaultValue = "50.0") Double threshold
    ) {
        // Make output more readable and do not allow scientific notation
        productService.summaryByThreshold(threshold).forEach(
                s -> System.out.println(
                        "Summary{uniqueProductCount=" + s.getUniqueProductCount() +
                                ", totalQuantity=" + s.getTotalQuantity() +
                                ", totalCost=" + s.getTotalCost() +
                                ", averageCost=" + s.getAverageCost() +
                                "}"));
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
            String rule
    ) {
        // Add additional validation
        String[] ruleParts = rule.split("->");
        Rule ruleEntity = ruleService.create(
                Rule.builder()
                        .expression(ruleParts[0])
                        .weight(Double.valueOf(ruleParts[1]))
                        .build());
        // Calculate all rule product scores and update overall product score
        productService.list().forEach(product -> {
            productRuleScoreService.calculate(ruleEntity, product);
            productScoreService.calculate(product.getProductScore());
        });
    }

    @ShellMethod(
            value = "Delete rule by id",
            key = "delete-rule",
            group = "Rule Commands"
    )
    public void RuleDelete(
            Long id
    ) {
        ruleService.deleteById(id);
        // Update overall product scores -- could move to a db trigger
        productService.list().forEach(product -> {
            productScoreService.calculate(product.getProductScore());
        });
        System.out.println("Deleted rule.");
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
