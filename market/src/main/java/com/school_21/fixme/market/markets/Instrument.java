package com.school_21.fixme.market.markets;

import lombok.Data;

import java.util.concurrent.ThreadLocalRandom;

@Data
public class Instrument {
    private String name;
    private String code;
    private Integer availableAmount;
    private Double minBuyPrice;
    private Double maxSellPrice;

    public Instrument(String code, String name){
        this.code = code;
        this.name = name;
        this.availableAmount = ThreadLocalRandom.current().nextInt(0, 1000);
        this.minBuyPrice = ThreadLocalRandom.current().nextDouble(0, 1000);
        this.maxSellPrice = ThreadLocalRandom.current().nextDouble(0, 1000);
    }

}
