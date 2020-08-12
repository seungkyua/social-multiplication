package com.ahnseungkyu.multiplication.service;

import com.ahnseungkyu.multiplication.domain.Multiplication;
import com.ahnseungkyu.multiplication.domain.MultiplicationResultAttempt;
import com.ahnseungkyu.multiplication.domain.User;
import com.ahnseungkyu.multiplication.repository.MultiplicationResultAttemptRepository;
import com.ahnseungkyu.multiplication.repository.UserRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class MultiplicationServiceImplTest {

    private MultiplicationServiceImpl multiplicationServiceImpl;

    @Mock
    private RandomGeneratorService randomGeneratorService;

    @Mock
    private MultiplicationResultAttemptRepository attemptRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        multiplicationServiceImpl = new MultiplicationServiceImpl(randomGeneratorService,
                attemptRepository, userRepository);
    }

    @Test
    public void createRandomMultiplicationTest() throws Exception {
        given(randomGeneratorService.generateRandomFactor()).willReturn(50, 30);

        Multiplication multiplication = multiplicationServiceImpl.createRandomMultiplication();

        assertThat(multiplication.getFactorA()).isEqualTo(50);
        assertThat(multiplication.getFactorB()).isEqualTo(30);
    }

    @Test
    public void checkCorrectAttemptTest() {

        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("Seungkyu");
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(
                user, multiplication, 3000, false);
        MultiplicationResultAttempt verifiedAttempt = new MultiplicationResultAttempt(
                user, multiplication, 3000, true);
        given(userRepository.findByAlias("Seungkyu")).willReturn(Optional.empty());

        boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

        assertThat(attemptResult).isTrue();
        verify(attemptRepository).save(verifiedAttempt);
    }

    @Test
    public void checkWrongAttemptTest() {

        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("Seungkyu");
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(
                user, multiplication, 3010, false);
        given(userRepository.findByAlias("Seungkyu")).willReturn(Optional.empty());

        boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

        assertThat(attemptResult).isFalse();
        verify(attemptRepository).save(attempt);
    }

    @Test
    public void retrieveStatsTest() {
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("Seungkyu");
        MultiplicationResultAttempt attempt1 = new MultiplicationResultAttempt(
                user, multiplication, 3010, false);
        MultiplicationResultAttempt attempt2 = new MultiplicationResultAttempt(
                user, multiplication, 3051, false);
        List<MultiplicationResultAttempt> lastestAttempts = Lists.newArrayList(attempt1, attempt2);
        given(userRepository.findByAlias("Seungkyu")).willReturn(Optional.empty());
        given(attemptRepository.findTop5ByUserAliasOrderByIdDesc("Seungkyu"))
                .willReturn(lastestAttempts);

        List<MultiplicationResultAttempt> latestAttemptResult =
                multiplicationServiceImpl.getStatsForUser("Seungkyu");

        assertThat(latestAttemptResult).isEqualTo(lastestAttempts);
    }

}
