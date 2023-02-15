package com.example.salesman.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductScore {

    @Id
    @Column(name = "product_id")
    private Long id;

    private Double score;

    @OneToOne(targetEntity = Product.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "product_id")
    @MapsId
    private Product product;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.MERGE)
    @JoinColumn(name = "product_id")
    private final Collection<ProductRuleScore> productRuleScores = new ArrayList<>();

    @Override
    public String toString() {
        return "ProductScore{" +
                "product_id=" + id +
                ", score=" + score +
                '}';
    }
}
