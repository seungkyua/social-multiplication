package com.ahnseungkyu.multiplication.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.IntStream.*;
import static org.assertj.core.api.Assertions.assertThat;

public class RandomGeneratorServiceImplTest {

    private RandomGeneratorServiceImpl randomGeneratorServiceImpl;

    @BeforeEach
    public void setUp() {
        randomGeneratorServiceImpl = new RandomGeneratorServiceImpl();
    }

    @Test
    public void generateRandomFactorIsBetweenExpectedLimits() throws Exception {
        List<Integer> randomFactors = range(0, 1000)
                .map(i -> randomGeneratorServiceImpl.generateRandomFactor())
                .boxed()
                .collect(Collectors.toList());

        assertThat(randomFactors).containsOnlyElementsOf(IntStream.range(11, 100)
                .boxed().collect(Collectors.toList()));
    }

}
