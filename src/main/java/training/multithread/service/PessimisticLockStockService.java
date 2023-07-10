package training.multithread.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training.multithread.domain.Stock;
import training.multithread.repository.StockRepository;

@Service
public class PessimisticLockStockService {
    private StockRepository stockRepository;

    public PessimisticLockStockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional
    public void decrease(Long id, Long quantity){
        Stock stock = stockRepository.findByIdWithPessimisticLock(id);
        stock.decrease(quantity);
        stockRepository.saveAndFlush(stock);
    }
}
