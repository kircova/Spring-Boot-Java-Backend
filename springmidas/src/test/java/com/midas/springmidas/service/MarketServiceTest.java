package com.midas.springmidas.service;

import com.midas.springmidas.repository.MarketRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MarketServiceTest {

    @Autowired
    private MarketRepository marketRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Test
    public void testSyncMarkets() {
        MarketService marketService = new MarketService(webClientBuilder, marketRepository);
        marketService.syncMarkets();
    }

}