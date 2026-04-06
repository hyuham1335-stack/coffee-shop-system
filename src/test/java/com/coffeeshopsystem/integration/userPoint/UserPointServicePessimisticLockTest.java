package com.coffeeshopsystem.integration.userPoint;

import com.coffeeshopsystem.domain.userpoint.entity.UserPoint;
import com.coffeeshopsystem.domain.userpoint.repository.PointHistoryRepository;
import com.coffeeshopsystem.domain.userpoint.repository.UserPointRepository;
import com.coffeeshopsystem.domain.userpoint.service.UserPointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserPointServicePessimisticLockTest {

    @Autowired
    private UserPointService userPointService;

    @Autowired
    private UserPointRepository userPointRepository;

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    @BeforeEach
    void setUp() {
        userPointRepository.deleteAll();
        pointHistoryRepository.deleteAll();

        UserPoint userPoint = new UserPoint(1L);
        userPoint.charge(new BigDecimal("5000"));
        userPointRepository.save(userPoint);
    }

    @Test
    void use_Point_withPessimisticLock_OnlyOneRequest_succeed() throws InterruptedException {
        int threadCount = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        Runnable task = () -> {
            readyLatch.countDown();
            try {
                startLatch.await();

                userPointService.usePoint(1L, new BigDecimal("4000"));
                successCount.incrementAndGet();

            } catch (Exception e) {
                failCount.incrementAndGet();
            } finally {
                doneLatch.countDown();
            }
        };

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(task);
        }

        readyLatch.await();
        startLatch.countDown();
        doneLatch.await();
        executorService.shutdown();

        UserPoint result = userPointRepository.findById(1L)
                .orElseThrow();

        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failCount.get()).isEqualTo(1);
        assertThat(result.getBalance()).isEqualByComparingTo("1000");
    }

}
