package com.example.salesman.repository;

import com.example.salesman.model.ProductScore;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductScoreRepository extends CrudRepository<ProductScore, Long> {
}
