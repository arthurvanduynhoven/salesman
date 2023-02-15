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
    private Double price;
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
                ", price=" + price +
                ", quantity=" + quantity +
                ", taxExempt=" + taxExempt +
                ", productScore=" + productScore.getScore() +
                '}';
    }
}
