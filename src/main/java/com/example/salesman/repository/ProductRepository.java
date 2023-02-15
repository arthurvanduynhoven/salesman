package com.example.salesman.repository;

import com.example.salesman.model.PurchaseSummary;
import com.example.salesman.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long>, PagingAndSortingRepository<Product, Long> {

    @Query(value = "SELECT " +
            "COUNT(*) as uniqueProductCount, " +
            "SUM(p.price * p.quantity) as totalCost, " +
            "SUM(p.quantity) as totalQuantity, " +
            "AVG(p.price) as averageCost " +
            "FROM Product as p " +
            "INNER JOIN " +
            "ProductScore as s ON s.id = p.id " +
            "WHERE s.score >= :score",
            nativeQuery = false)
    List<PurchaseSummary> countProductPricesByProductScore_ScoreGreaterThanEqual(Double score);

    List<Product> getProductsByProductScore_ScoreGreaterThanEqual(Double score);
}
