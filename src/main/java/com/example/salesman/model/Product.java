package com.example.salesman.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String productRef;
    private String name;
    private String color;
    private Double cost;
    private Integer quantity;
    private Boolean taxExempt;

    @OneToOne(targetEntity = ProductScore.class, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private ProductScore productScore;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productRef='" + productRef + '\'' +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", cost=" + cost +
                ", quantity=" + quantity +
                ", taxExempt=" + taxExempt +
//                ", score=" + score +
                ", score=" + productScore.getScore() +
                '}';
    }
}
