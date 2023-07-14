package com.midas.springmidas.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.midas.springmidas.entity.Market;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

//@DataJpaTest
@SpringBootTest
class MarketRepositoryTest {

    @Autowired
    private MarketRepository marketRepository;

    @Test
    public void printAllMarket() {
        List<Market> marketList =
                marketRepository.findAll();

        System.out.println("Market List = " + marketList);
    }
}