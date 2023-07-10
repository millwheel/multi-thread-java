package training.multithread.service;

import org.springframework.stereotype.Service;
import training.multithread.repository.StockRepository;

@Service
public class StockService {
    private StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void decrease(Long id, Long quantity){
        stockRepository.findById(id).orElseThrow();
    }
}