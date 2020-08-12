package com.ahnseungkyu.multiplication.service;

import com.ahnseungkyu.multiplication.domain.Multiplication;
import com.ahnseungkyu.multiplication.domain.MultiplicationResultAttempt;
import com.ahnseungkyu.multiplication.repository.MultiplicationResultAttemptRepository;
import com.ahnseungkyu.multiplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

@Service("multiplicationService")
public class MultiplicationServiceImpl implements MultiplicationService {

    private RandomGeneratorService randomGeneratorService;
    private MultiplicationResultAttemptRepository attemptRepository;
    private UserRepository userRepository;

    @Autowired
    public MultiplicationServiceImpl(
            final RandomGeneratorService randomGeneratorService,
            final MultiplicationResultAttemptRepository attemptRepository,
            final UserRepository userRepository) {
        this.randomGeneratorService = randomGeneratorService;
        this.attemptRepository = attemptRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Multiplication createRandomMultiplication() {
        int factorA = randomGeneratorService.generateRandomFactor();
        int factorB = randomGeneratorService.generateRandomFactor();
        return new Multiplication(factorA, factorB);
    }

    @Transactional
    @Override
    public boolean checkAttempt(final MultiplicationResultAttempt attempt) {
        boolean correct = attempt.getResultAttempt() ==
                attempt.getMultiplication().getFactorA() *
                attempt.getMultiplication().getFactorB();

        Assert.isTrue(!attempt.isCorrect(), "채점한 상태로 보낼 수 없습니다!!");

        MultiplicationResultAttempt checkedAttempt = new MultiplicationResultAttempt(
                attempt.getUser(), attempt.getMultiplication(), attempt.getResultAttempt(),
                correct);

        attemptRepository.save(checkedAttempt);

        return correct;
    }

    @Override
    public List<MultiplicationResultAttempt> getStatsForUser(String userAlias) {
        return attemptRepository.findTop5ByUserAliasOrderByIdDesc(userAlias);
    }
}
