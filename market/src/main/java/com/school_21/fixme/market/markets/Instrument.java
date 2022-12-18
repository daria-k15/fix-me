package com.school_21.fixme.market.markets;

import lombok.Data;

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
    }

}
