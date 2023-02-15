package com.example.salesman.model;

public interface IPurchaseSummary {
    Long getUniqueProductCount();

    Integer getTotalQuantity();

    Double getTotalCost();

    Double getAverageCost();
}
