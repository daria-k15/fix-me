package com.school_21.fixme.utils.orders;

import com.school_21.fixme.utils.messages.Message;
import lombok.Data;

@Data
public class Orders {
    private boolean valid = true;
    private String market;
    private String instrument;
    private String quantity;
    private String price;
    private String clientId;

    public Orders(Message message) {
        this.market =     message.get("103");
        this.instrument = message.get("100");
        this.quantity =     message.get("101");
        this.price =      message.get("102");
        this.clientId =   message.get("553");
        this.validateOrder();
    }

    private void validateOrder(){
        if (this.quantity.contains("-") || this.price.contains("-")){
            this.valid = false;
        }
    }
}
