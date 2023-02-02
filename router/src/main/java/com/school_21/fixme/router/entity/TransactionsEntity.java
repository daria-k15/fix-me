package com.school_21.fixme.router.entity;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class TransactionsEntity {
    private int id;
    private String msgLength;
    private Integer msgType;
    private ZonedDateTime date;
    private String username;
    private String itemId;
    private String amount;
    private Double price;
    private String marketName;
    private String checkSum;
}
