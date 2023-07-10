package training.multithread.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import training.multithread.domain.Stock;
import training.multithread.repository.StockRepository;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class PessimisticLockStockServiceTest {

    private final PessimisticLockStockService pessimisticLockStockService;
    private final StockRepository stockRepository;

    @Autowired
    PessimisticLockStockServiceTest(PessimisticLockStockService pessimisticLockStockService, StockRepository stockRepository) {
        this.pessimisticLockStockService = pessimisticLockStockService;
        this.stockRepository = stockRepository;
    }

    @BeforeEach
    public void before(){
        Stock stock = new Stock(1L, 100L);
        stockRepository.saveAndFlush(stock);
    }

    @AfterEach
    public void after(){
        stockRepository.deleteAll();
    }

    @Test
    public void concurrent_decrease() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++){
            executorService.submit(()->{
                try {
                    pessimisticLockStockService.decrease(1L, 1L);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();
        Assertions.assertEquals(0, stock.getQuantity());
    }
}