package com.school_21.fixme.utils.orders;

public enum OrderType {
    BUY("B"),
    SELL("S"),

    LOGON("L"),
    ERROR("E");

    public final String value;

    OrderType(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return value;
    }
}
