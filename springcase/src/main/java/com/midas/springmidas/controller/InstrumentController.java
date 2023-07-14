package com.midas.springmidas.controller;

import com.midas.springmidas.entity.InstrumentDTO;
import com.midas.springmidas.service.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/instruments")
public class InstrumentController {
    private final InstrumentService instrumentService;

    @Autowired
    public InstrumentController(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    @GetMapping("/sync")
    public void syncInstruments(){
        instrumentService.syncInstruments();
    }

    @GetMapping("/getall")
    public List<InstrumentDTO> getAllInstruments(){
        return instrumentService.getAllInstruments();
    }

    @GetMapping("/{symbol}")
    public InstrumentDTO getInstrumentBySymbol(@PathVariable String symbol) {
        System.out.println("Getting instrument by symbol: " + symbol);
        return instrumentService.getInstrumentBySymbol(symbol);
    }

}
