package training.multithread.facade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training.multithread.repository.LockRepository;
import training.multithread.service.StockService;

@Service
public class NamedLockStockFacade {
    private final LockRepository lockRepository;
    private final StockService stockService;

    public NamedLockStockFacade(LockRepository lockRepository, StockService stockService) {
        this.lockRepository = lockRepository;
        this.stockService = stockService;
    }

    @Transactional
    public void decrease(Long id, Long quantity){
        try{
            lockRepository.getLock(id.toString());
            stockService.decrease(id, quantity);
        } finally {
            lockRepository.releaseLock(id.toString());
        }
    }
}
