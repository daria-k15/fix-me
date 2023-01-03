package com.school_21.fixme.utils;

import com.school_21.fixme.utils.exceptions.InvalidBodyLengthException;
import com.school_21.fixme.utils.exceptions.InvalidCheckSumException;
import com.school_21.fixme.utils.exceptions.InvalidCodePosition;
import com.school_21.fixme.utils.messages.Message;
import com.school_21.fixme.utils.orders.OrderType;
import com.school_21.fixme.utils.orders.Orders;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

public class FixProtocol {
    private static final String MSG_HEADER = "8=FIX.21|";
    private static final String MSG_TYPE = "35=";
    private static final String MSG_CREATED_TIME = "52=";
    private static final String MSG_BODY_LENGTH = "9=";
    private static final String MSG_CHECKSUM = "10=";
    private static final String USERNAME = "553=";
    private static final String HEARTBEAT = "108=";
    private static final String MSG_DELIMITER = "|";
    private static final String BROKER_ID = "554=";
    private static final String INVALID_REQUEST = "560=1|";
    private static final String ITEM_ID = "100=";
    private static final String AMOUNT = "101=";
    private static final String PRICE = "102=";
    private static final String MARKET_NAME = "103=Crypto";

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static void validateMessage(Message fixMsg) throws InvalidCheckSumException, InvalidBodyLengthException {
        try {
            checkSumValidator(fixMsg.getRawMessage());
            msgBodyLengthValidator(fixMsg.getRawMessage());
        } catch (InvalidCheckSumException e) {
            System.out.println("Checksum");
            throw new InvalidCheckSumException("Invalid checkSum!");
        } catch (InvalidBodyLengthException e) {
            System.out.println("Length");
            throw new InvalidBodyLengthException("Invalid bodyLength!");
        }
    }

    public static void checkSumValidator(String input) throws InvalidCheckSumException {
        String[] values = input.split("\\|10=");

        if (values.length != 2) {
            throw new InvalidCheckSumException("Invalid checksum!");
        }

        values[0] += "|";
        String checkSum = checkSumGenerator(values[0]);
        if (!checkSum.equals(values[1])) {
            throw new InvalidCheckSumException("Invalid checksum");
        }
    }

    public static String checkSumGenerator(String input) {
        String msg = input.replace(MSG_DELIMITER, "\u0001");
        byte[] msgBytes = msg.getBytes(StandardCharsets.US_ASCII);

        int total = 0;
        for (int i = 0; i < msgBytes.length; i++) {
            total += msgBytes[i];
        }

        Integer checkSum = total % 256;

        return StringUtils.leftPad(checkSum.toString(), 3, "0");
    }

    private static void msgBodyLengthValidator(String messageInput) throws InvalidBodyLengthException {
        int msgLength = 0;

        if (!messageInput.contains(MSG_DELIMITER + MSG_BODY_LENGTH)) {
            throw new InvalidBodyLengthException("Incorrect Message Length");
        }
        int msgIndexStart = messageInput.indexOf(MSG_DELIMITER + MSG_BODY_LENGTH) + 3;
        while (messageInput.charAt(msgIndexStart) != '|') {
            msgIndexStart++;
        }
        msgIndexStart++;

        int msgIndexEnd = messageInput.indexOf(MSG_DELIMITER + MSG_CHECKSUM);
        msgIndexEnd++;

        String innerMessage = messageInput.substring(msgIndexStart, msgIndexEnd);

        String[] message = messageInput.split("\\|");
        for (int i = 0; i < message.length; i++) {
            if (message[i].startsWith(MSG_BODY_LENGTH) &&
                    isNumeric(message[i].substring(2)) && isInteger(message[i].substring(2))) {
                msgLength = Integer.parseInt(message[i].substring(2));
            }
        }
        if (msgLength < 0 || msgLength != innerMessage.length()) {
            throw new InvalidBodyLengthException("Incorrect Message Length");
        }
    }

    public static String constructMessage(String body, OrderType type) {
        //Protocol Version|length|Message Type|Message Sequence Number|Date|

        StringBuilder header = new StringBuilder();
        header.append(MSG_HEADER);
        StringBuilder msg = new StringBuilder();
        msg.append(MSG_TYPE + type.value + MSG_DELIMITER);
        msg.append(MSG_CREATED_TIME + OffsetDateTime.now() + MSG_DELIMITER);
        int length = body.length() + msg.length();
        header.append(MSG_BODY_LENGTH + length + MSG_DELIMITER);
        header.append(msg);
        return header.toString();
    }

//    public String logonMessage(int heartBeat) {
//        //Encryption|UserID|Heartbeat|resetSeqNum|
//
//        StringBuilder body = new StringBuilder();
//        if (heartBeat > 0) {
//            body.append(HEARTBEAT + heartBeat + MSG_DELIMITER);
//        } else {
//            body.append(HEARTBEAT + "120|"); // timeout
//        }
//        String header = constructMessage(body.toString(), "A"); //type A - logon
//        return header + body + MSG_CHECKSUM + checkSumGenerator(header + body + MSG_DELIMITER);
//    }

//    public String heartBeatMessage(int brokerRouteId) {
//        StringBuilder body = new StringBuilder();
//
//        body.append(USERNAME + this.userId + MSG_DELIMITER);
//        body.append(BROKER_ID + brokerRouteId + MSG_DELIMITER);
//        body.append(INVALID_REQUEST);
//        String header = constructMessage(body.toString(), "0", this.msgSeqNum++); // invalid request
//        return header + body + MSG_CHECKSUM + checkSumGenerator(header + body + MSG_DELIMITER);
//    }

    public static Message orderMessage(String userId, String itemId, String amount, String price, String brokerRouteId, OrderType type) {
        StringBuilder body = new StringBuilder();
        body.append(USERNAME + userId + MSG_DELIMITER);
        body.append(BROKER_ID + brokerRouteId + MSG_DELIMITER);
        body.append(ITEM_ID + itemId + MSG_DELIMITER);
        body.append(AMOUNT + amount + MSG_DELIMITER);
        body.append(PRICE + price + MSG_DELIMITER);
        body.append(MARKET_NAME + MSG_DELIMITER);
        String header = constructMessage(body.toString(), type);
        return new Message(header + body + MSG_CHECKSUM + checkSumGenerator(header + body));
    }

//    public Message saleMessage(String userId, String itemId, String amount, String price, String brokerRouteId) {
//        StringBuilder body = new StringBuilder();
//        body.append(USERNAME + userId + MSG_DELIMITER);
//        body.append(BROKER_ID + brokerRouteId + MSG_DELIMITER);
//        body.append(ITEM_ID + itemId + MSG_DELIMITER);
//        body.append(AMOUNT + amount + MSG_DELIMITER);
//        body.append(PRICE + price + MSG_DELIMITER);
//
//        String header = constructMessage(body.toString(), "2");
//        return new Message(header + body + MSG_CHECKSUM + checkSumGenerator(header + body + MSG_DELIMITER));
//    }

    public static Message logonMessage(String id) {
        StringBuilder body = new StringBuilder();
        body.append(USERNAME + id + MSG_DELIMITER);
        String header = constructMessage(body.toString(), OrderType.LOGON);
        return new Message(header + body + MSG_CHECKSUM + checkSumGenerator(header + body));
    }

    public static Message failResponse(String id){
        StringBuilder body = new StringBuilder();
        body.append(USERNAME + id + MSG_DELIMITER);
        String header = constructMessage(body.toString(), OrderType.ERROR);
        return new Message(header + body + MSG_CHECKSUM + checkSumGenerator(header + body));
    }

    public static Message identifyMessage(String id) {
        StringBuilder body = new StringBuilder();
        body.append(USERNAME + id + MSG_DELIMITER);
        body.append(MARKET_NAME + MSG_DELIMITER);
        String header = constructMessage(body.toString(), OrderType.IDENTIFY);
        return new Message(header + body + MSG_CHECKSUM + checkSumGenerator(header + body));
    }

    public static Message rejectOrder(Orders order) {
        StringBuilder body = new StringBuilder();
        body.append(USERNAME + order.getClientId() + MSG_DELIMITER);
        body.append(MARKET_NAME + MSG_DELIMITER);
        String header = constructMessage(body.toString(), OrderType.REJECT);
        return new Message(header + body + MSG_CHECKSUM + checkSumGenerator(header + body));
    }

    public static Message acceptOrder(Orders order) {
        StringBuilder body = new StringBuilder();
        body.append(USERNAME + order.getClientId() + MSG_DELIMITER);
        body.append(MARKET_NAME + MSG_DELIMITER);
        String header = constructMessage(body.toString(), OrderType.ACCEPT);
        return new Message(header + body + MSG_CHECKSUM + checkSumGenerator(header + body));
    }
}
