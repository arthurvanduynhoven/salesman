package com.example.salesman.service;

import com.example.salesman.model.Rule;
import com.example.salesman.repository.RuleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RuleService {
    private final RuleRepository ruleRepository;

    public Rule create(Rule rule) {
        return ruleRepository.save(rule);
    }

    public void deleteById(Long id) {
        ruleRepository.deleteById(id);
    }

    // TODO: Add filter args from cli
    public Iterable<Rule> list() {
        return ruleRepository.findAll();
    }
}
