package com.midas.springmidas.repository;

import com.midas.springmidas.entity.Instrument;
import com.midas.springmidas.entity.Market;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InstrumentRepositoryTest {

    @Autowired
    private MarketRepository marketRepository;

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Test
    public void displayAllInstruments() {
        List<Instrument> instruments = instrumentRepository.findAll();
    }

    @Test
    public void displayAllInstrumentsByMarket() {
        Market market = marketRepository.findById(44L).get();
        List<Instrument> instruments = instrumentRepository.findByMarket(market);
    }

}