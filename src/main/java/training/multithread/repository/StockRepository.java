package training.multithread.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import training.multithread.domain.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {

}
