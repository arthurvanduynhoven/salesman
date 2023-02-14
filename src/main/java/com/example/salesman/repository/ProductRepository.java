package com.example.salesman.repository;

import com.example.salesman.model.Product;
import com.example.salesman.model.IPurchaseSummary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long>, PagingAndSortingRepository<Product, Long> {

    @Query(value = "SELECT " +
            "COUNT(*) as count, " +
            "SUM(p.cost * p.quantity) as totalCost, " +
            "AVG(p.cost) as averageCost " +
            "FROM Product as p " +
            "INNER JOIN " +
            "ProductScore as s ON s.id = p.id " +
            "WHERE s.score >= :score",
            nativeQuery = false)
    List<IPurchaseSummary> countProductPricesByProductScore_ScoreGreaterThanEqual(Double score);

    List<Product> getProductsByProductScore_ScoreGreaterThanEqual(Double score);
}
