package com.ahnseungkyu.multiplication.repository;

import com.ahnseungkyu.multiplication.domain.Multiplication;
import org.springframework.data.repository.CrudRepository;

public interface MultiplicationRepository extends CrudRepository<Multiplication, Long> {
}
