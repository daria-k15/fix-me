package com.school_21.fixme.market.markets;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Market {
    private String id;
    private String name;
    private List<Instrument> instruments = new ArrayList<>();

    public Market(String name) {
        this.name = name;
    }

    public Instrument instrumentByCode(String name) {
        return instruments.stream().filter(it ->
                it.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
