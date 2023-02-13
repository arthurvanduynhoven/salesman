package com.example.salesman.service;

import com.example.salesman.model.IPurchaseSummary;
import com.example.salesman.model.Product;
import com.example.salesman.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product create(Product product) {
        return productRepository.save(product);
    }

    // TODO: Add filter args from cli
    public Iterable<Product> list() {
        return productRepository.findAll();
    }

    public Iterable<Product> listByThreshold(Double threshold) {
        return productRepository.getProductsByProductScore_ScoreGreaterThanEqual(threshold);
    }

    public List<IPurchaseSummary> summaryByThreshold(Double threshold) {
        return productRepository.countProductPricesByProductScore_ScoreGreaterThanEqual(threshold);
    }
}
