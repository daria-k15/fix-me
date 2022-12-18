package com.school_21.fixme.market.markets;

public class CryptoMarket extends Market{
    public CryptoMarket() {
        super("Crypto");
        getInstruments().add(new Instrument("BTC", "Bitcoin"));
        getInstruments().add(new Instrument("ETH", "Ethereum"));
        getInstruments().add(new Instrument("USDT", "Tether"));
        getInstruments().add(new Instrument("LTC", "Litecoin"));
        getInstruments().add(new Instrument("SHIB", "Shiba Inu"));
        getInstruments().add(new Instrument("APE", "ApeCoin"));
    }
}
