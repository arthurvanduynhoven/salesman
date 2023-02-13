package com.example.salesman.repository;

import com.example.salesman.model.Rule;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleRepository extends CrudRepository<Rule, Long>, PagingAndSortingRepository<Rule, Long> {
}
