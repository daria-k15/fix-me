package com.school_21.fixme.utils;

import com.school_21.fixme.utils.exceptions.InvalidBodyLengthException;
import com.school_21.fixme.utils.exceptions.InvalidCheckSumException;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

public class FixProtocol {
    private String userId;
    private Integer msgSeqNum;
    private static final String MSG_HEADER = "8=FIX.21|";
    private static final String MSG_TYPE = "35=";
    private static final String MSG_SEQ_NUM = "34=";
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

    public FixProtocol(String userId){
        this.userId = userId;
        this.msgSeqNum = 0;
    }

    public static boolean isNumeric(String str){
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException e){
            return false;
        }
        return true;
    }

    public static boolean isInteger(String str){
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e){
            return false;
        }
        return true;
    }

    public int validateMessage(String fixMsg){
        try {
            checkSumValidator(fixMsg);
            msgBodyLengthValidator(fixMsg);
        } catch (InvalidCheckSumException e){
            return -1;
        } catch (InvalidBodyLengthException e){
            return -2;
        }
        return 1;
    }

    private void checkSumValidator(String input) throws InvalidCheckSumException {
        String[] values = input.split(MSG_DELIMITER + MSG_CHECKSUM);

        if (values.length != 2){
            throw new InvalidCheckSumException("Invalid checksum!");
        }

        values[0] += "|";
        String checkSum = checkSumGenerator(values[0]);
        if (!checkSum.equals(values[1])){
            throw new InvalidCheckSumException("Invalid checksum");
        }
//        return true;
    }

    private String checkSumGenerator(String input){
        String msg = input.replace(MSG_DELIMITER, "\u0001");
        byte[] msgBytes = msg.getBytes(StandardCharsets.US_ASCII);

        int total = 0;
        for (int i = 0; i < msgBytes.length; i++){
            total += msgBytes[i];
        }

        Integer checkSum = total % 256;

        return StringUtils.leftPad(checkSum.toString(), 3, "0");
    }

    public void msgBodyLengthValidator(String messageInput) throws InvalidBodyLengthException {
        int msgLength = 0;

        if (!messageInput.contains(MSG_DELIMITER + MSG_BODY_LENGTH)) {
            throw new InvalidBodyLengthException("Incorrect Message Length");
        }
        int msgIndexStart = messageInput.indexOf(MSG_DELIMITER + MSG_BODY_LENGTH) + 3;
        while (messageInput.charAt(msgIndexStart) != '|') { msgIndexStart++;}
        msgIndexStart++;

        int msgIndexEnd = messageInput.indexOf(MSG_DELIMITER + MSG_CHECKSUM);
        msgIndexEnd++;

        String innerMessage = messageInput.substring(msgIndexStart, msgIndexEnd);

        String[] message = messageInput.split("\\|");
        for (int i=0; i < message.length; i++) {
            if (message[i].startsWith(MSG_BODY_LENGTH) &&
                    isNumeric(message[i].substring(2)) && isInteger(message[i].substring(2))) {
                msgLength = Integer.parseInt(message[i].substring(2));
            }
        }
        if (msgLength < 0 || msgLength != innerMessage.length()) {
            throw new InvalidBodyLengthException("Incorrect Message Length");
        }
//        return true;
    }

    public String constructMessage(String body, String type, int msgSeqNum){
        //Protocol Version|length|Message Type|Message Sequence Number|Date|

        StringBuilder header = new StringBuilder();
        header.append(MSG_HEADER);
        StringBuilder msg = new StringBuilder();
        msg.append(MSG_TYPE + type + MSG_DELIMITER);
        msg.append(MSG_SEQ_NUM+ msgSeqNum + MSG_DELIMITER);
        msg.append(MSG_CREATED_TIME + OffsetDateTime.now() + MSG_DELIMITER);
        int length = body.length() + msg.length();
        header.append(MSG_BODY_LENGTH + length + MSG_DELIMITER);
        header.append(msg);
        return header.toString();
    }

    public String logonMessage(int heartBeat) {
        //Encryption|UserID|Heartbeat|resetSeqNum|

        StringBuilder body = new StringBuilder();
        body.append(USERNAME + this.userId + MSG_DELIMITER);
        if (heartBeat > 0) {
            body.append(HEARTBEAT + heartBeat + MSG_DELIMITER);
        } else {
            body.append(HEARTBEAT + "120|"); // timeout
        }
        String header = constructMessage(body.toString(), "A", this.msgSeqNum); //type A - logon
        return header + body + MSG_CHECKSUM + checkSumGenerator(header + body + MSG_DELIMITER);
    }

    public String heartBeatMessage(int brokerRouteId){
        StringBuilder body = new StringBuilder();

        body.append(USERNAME + this.userId + MSG_DELIMITER);
        body.append(BROKER_ID + brokerRouteId + MSG_DELIMITER);
        body.append(INVALID_REQUEST);
        String header = constructMessage(body.toString(), "0", this.msgSeqNum++); // invalid request
        return header + body + MSG_CHECKSUM + checkSumGenerator(header + body + MSG_DELIMITER);
    }

    public String purchaseMessage(String itemId, String amount, String price, String brokerRouteId){
        StringBuilder body = new StringBuilder();
        body.append(USERNAME + this.userId + MSG_DELIMITER);
        body.append(BROKER_ID + brokerRouteId + MSG_DELIMITER);
        body.append(ITEM_ID + itemId + MSG_DELIMITER);
        body.append(AMOUNT + amount + MSG_DELIMITER);
        body.append(PRICE + price + MSG_DELIMITER);
        String header = constructMessage(body.toString(), "1", ++this.msgSeqNum);
        return header + body + MSG_CHECKSUM + checkSumGenerator(header + body + MSG_DELIMITER);
    }

    public String saleMessage(String itemId, String amount, String price, String brokerRouteId) {
        StringBuilder body = new StringBuilder();
        body.append(USERNAME + this.userId + MSG_DELIMITER);
        body.append(BROKER_ID + brokerRouteId + MSG_DELIMITER);
        body.append(ITEM_ID + itemId +MSG_DELIMITER);
        body.append(AMOUNT + amount + MSG_DELIMITER);
        body.append(PRICE + price + MSG_DELIMITER);

        String header = constructMessage(body.toString(), "2", ++this.msgSeqNum);
        return header + body + MSG_CHECKSUM + checkSumGenerator(header + body + MSG_DELIMITER);
    }

    public String logonMessage(String id){
        StringBuilder body = new StringBuilder();
        body.append(USERNAME + id + MSG_DELIMITER);
        String header = constructMessage(body.toString(), "L", ++this.msgSeqNum);
        return header + body + MSG_CHECKSUM + checkSumGenerator(header + body + MSG_DELIMITER);
    }

    public List<String> initLogon(String msg){
        return Arrays.asList(msg.split("\\|"));
    }
}
