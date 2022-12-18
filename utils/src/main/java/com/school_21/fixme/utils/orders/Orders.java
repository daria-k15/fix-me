package com.school_21.fixme.utils.orders;

public class Orders {
    private boolean valid = true;
    private String market;
    private String instrument;
    private String quantity;
    private String price;
    private String clientId;

    public Orders(String market, String instrument, String quantity, String price, String clientId){
        this.market = market;
        this.instrument = instrument;
        this.quantity = quantity;
        this.price = price;
        this.clientId = clientId;
        this.validateOrder();
    }

    private void validateOrder(){
        if (this.quantity.contains("-") || this.price.contains("-")){
            this.valid = false;
        }
    }
}
