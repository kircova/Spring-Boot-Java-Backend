package com.midas.springmidas.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.midas.springmidas.entity.Instrument;
import com.midas.springmidas.entity.InstrumentDTO;
import com.midas.springmidas.entity.Market;
import com.midas.springmidas.repository.InstrumentRepository;
import com.midas.springmidas.repository.MarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InstrumentService {
    private final WebClient.Builder webClientBuilder;
    private final InstrumentRepository instrumentRepository;
    private final MarketRepository marketRepository;

    @Autowired
    public InstrumentService(WebClient.Builder webClientBuilder, InstrumentRepository instrumentRepository, MarketRepository marketRepository) {
        this.webClientBuilder = webClientBuilder;
        this.instrumentRepository = instrumentRepository;
        this.marketRepository = marketRepository;

    }

    public void syncInstruments() {

        try {
            // Retrieve existing instruments from the database
            List<Instrument> existingInstruments = instrumentRepository.findAll();
            List<Market> existingMarkets = marketRepository.findAll();

            Flux.fromIterable(existingInstruments)
                    .flatMap(instrumentNode -> {
                        String code = instrumentNode.getSymbol();
                        String url = "https://api.robinhood.com/instruments/?symbol=" + code;

                        return webClientBuilder.build()
                                .get()
                                .uri(url)
                                .retrieve()
                                .bodyToMono(String.class)
                                .map(instrumentString -> {
                                    ObjectMapper objectMapper = new ObjectMapper();
                                    try {
                                        JsonNode responseJson = objectMapper.readTree(instrumentString);
                                        JsonNode resultsNode = responseJson.get("results");

                                        if (resultsNode.isArray() && resultsNode.size() > 0) {
                                            JsonNode firstElement = resultsNode.get(0);
                                            instrumentNode.setSymbol(firstElement.get("symbol").asText());
                                            instrumentNode.setName(firstElement.get("name").asText());
                                            instrumentNode.setCustomName(firstElement.get("simple_name").asText());

                                            // Setting the market field of the instrument by checking the MARKET URL
                                            String marketUrl = firstElement.get("market").asText();
                                            String marketCode = marketUrl.substring(0, marketUrl.lastIndexOf('/'));
                                            marketCode = marketCode.substring(marketCode.lastIndexOf('/') + 1);
                                            String finalMarketCode = marketCode;

                                            Optional<Market> existingMarketOptional = existingMarkets.stream()
                                                    .filter(market -> market.getCode().equals(finalMarketCode))
                                                    .findFirst();
                                            existingMarketOptional.ifPresent(instrumentNode::setMarket);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    System.out.println("Instrument Node " + instrumentNode);
                                    return instrumentNode;
                                });
                    })
                    .collectList()
                    .doOnNext(instrumentRepository::saveAll)
                    .block();

            System.out.println("Instrument Sync Completed.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public List<InstrumentDTO> getAllInstruments() {
        List<Instrument> instruments = instrumentRepository.findAll();

        return instruments.stream()
                .sorted(Comparator.comparing(Instrument::getId))
                .map(instrument -> {
                    InstrumentDTO instrumentDTO = new InstrumentDTO();
                    instrumentDTO.setId(instrument.getId());
                    instrumentDTO.setSymbol(instrument.getSymbol());
                    instrumentDTO.setName(instrument.getName());
                    instrumentDTO.setCustomName(instrument.getCustomName());

                    // Set only the market_id of the market field
                    if (instrument.getMarket() != null) {
                        instrumentDTO.setMarketId(instrument.getMarket().getMarketId());
                    }

                    return instrumentDTO;
                })
                .collect(Collectors.toList());
    }

    //Write a method to get instrument by given symbol

    @Cacheable(value = "instruments", key = "#symbol")
    public InstrumentDTO getInstrumentBySymbol(String symbol) {
        Instrument instrument = instrumentRepository.findBySymbol(symbol)
                .orElseThrow(() -> new RuntimeException("Instrument not found"));

        InstrumentDTO instrumentDTO = new InstrumentDTO();
        instrumentDTO.setId(instrument.getId());
        instrumentDTO.setSymbol(instrument.getSymbol());
        instrumentDTO.setName(instrument.getName());
        instrumentDTO.setCustomName(instrument.getCustomName());
        instrumentDTO.setMarketId(instrument.getMarket().getMarketId());

        System.out.println("Instrument fetched from DB | Not Cached.");
        return instrumentDTO;
    }
}
