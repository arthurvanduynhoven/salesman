package com.example.salesman.model;

public interface PurchaseSummary {
    Long getUniqueProductCount();

    Integer getTotalQuantity();

    Double getTotalCost();

    Double getAverageCost();
}
