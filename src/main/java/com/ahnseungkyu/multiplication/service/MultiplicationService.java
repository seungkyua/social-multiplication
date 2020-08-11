package com.ahnseungkyu.multiplication.service;

import com.ahnseungkyu.multiplication.domain.Multiplication;
import com.ahnseungkyu.multiplication.domain.MultiplicationResultAttempt;

public interface MultiplicationService {

    Multiplication createRandomMultiplication();

    boolean checkAttempt(final MultiplicationResultAttempt resultAttempt);
}
