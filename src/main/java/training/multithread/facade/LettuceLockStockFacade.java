package training.multithread.facade;

import org.springframework.stereotype.Service;
import training.multithread.repository.RedisLockRepository;
import training.multithread.service.StockService;

@Service
public class LettuceLockStockFacade {

    private RedisLockRepository redisLockRepository;

    private StockService stockService;

    public LettuceLockStockFacade(RedisLockRepository redisLockRepository) {
        this.redisLockRepository = redisLockRepository;
    }

    public void decrease(Long key, Long quantity) throws InterruptedException {
        while(!redisLockRepository.lock(key)){
            Thread.sleep(100);
        }
        try{
            stockService.decrease(key, quantity);
        } finally {
            redisLockRepository.unlock(key);
        }
    }
}
