package com.midas.springmidas.service;

import com.midas.springmidas.entity.InstrumentDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class InstrumentServiceTest {

    @Autowired
    private InstrumentService instrumentService;

    @Test
    public void getAllInstruments_ShouldReturnAllInstruments() {
        List<InstrumentDTO> instruments = instrumentService.getAllInstruments();
    }

    @Test
    public void getInstrumentBySymbol_ExistingSymbol_ShouldReturnInstrumentDTO() {
        InstrumentDTO instrumentDTO = instrumentService.getInstrumentBySymbol("AAPL");
    }

}