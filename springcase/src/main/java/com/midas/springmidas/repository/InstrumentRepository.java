package com.midas.springmidas.repository;

import com.midas.springmidas.entity.Instrument;
import com.midas.springmidas.entity.Market;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, Long>{
    Optional<Instrument> findBySymbol(String symbol);

    List<Instrument> findByMarket(Market market);
}
