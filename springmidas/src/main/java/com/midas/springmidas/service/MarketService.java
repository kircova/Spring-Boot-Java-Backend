package com.midas.springmidas.service;
import com.fasterxml.jackson.databind.JsonNode;
import com.midas.springmidas.entity.Market;
import com.midas.springmidas.repository.MarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

@Service
public class MarketService {

    private final WebClient.Builder webClientBuilder;
    private final MarketRepository marketRepository;

    @Autowired
    public MarketService(WebClient.Builder webClientBuilder, MarketRepository marketRepository) {
        this.webClientBuilder = webClientBuilder;
        this.marketRepository = marketRepository;
    }

    public void syncMarkets() {

        String marketString = webClientBuilder.build()
                .get()
                .uri("https://api.robinhood.com/markets/")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        try {

            //System.out.println("Response JSON: " + marketString);
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode responseJson = objectMapper.readTree(marketString);
            JsonNode resultsNode = responseJson.get("results");

            // Retrieve existing markets from the database
            List<Market> existingMarkets = marketRepository.findAll();

            // Identify changes or new markets
            if (resultsNode != null && resultsNode.isArray() && resultsNode.size() > 0) {
                for (JsonNode marketNode : resultsNode) {

                    String code = marketNode.get("mic").asText();
                    // Check if a market with the same code exists in the database
                    Optional<Market> existingMarketOptional = existingMarkets.stream()
                            .filter(market -> market.getCode().equals(code))
                            .findFirst();

                    if(existingMarketOptional.isPresent()) {
                        Market existingMarket = existingMarketOptional.get();
                        existingMarket.setSymbol(marketNode.get("acronym").asText());
                        existingMarket.setName(marketNode.get("name").asText());
                        existingMarket.setCountry(marketNode.get("country").asText());
                        existingMarket.setWebsite(marketNode.get("website").asText());
                    }
                    else {
                        Market newMarket = Market.builder()
                                .Code(marketNode.get("mic").asText())
                                .Symbol(marketNode.get("acronym").asText())
                                .Name(marketNode.get("name").asText())
                                .Country(marketNode.get("country").asText())
                                .Website(marketNode.get("website").asText())
                                .build();
                        existingMarkets.add(newMarket);
                    }
                }

                System.out.println("Response JSON: " + existingMarkets);
                marketRepository.saveAll(existingMarkets);
            }

            //JavaType marketListType = typeFactory.constructCollectionType(List.class, Market.class);
            //List<Market> marketList = objectMapper.readValue(resultsNode.toString(), marketListType);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
