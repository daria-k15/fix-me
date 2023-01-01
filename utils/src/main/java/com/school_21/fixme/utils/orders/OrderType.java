package com.school_21.fixme.utils.orders;

public enum OrderType {
    BUY("1"),
    SELL("2"),

    LOGON("L"),
    ERROR("E"),
    IDENTIFY("I");

    public final String value;

    OrderType(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return value;
    }
}
