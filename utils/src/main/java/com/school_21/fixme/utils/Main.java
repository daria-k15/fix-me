package com.school_21.fixme.utils;

import com.school_21.fixme.utils.exceptions.InvalidBodyLengthException;


public class Main {
    private static final String MSG_DELIMITER = "|";
    private static final String MSG_BODY_LENGTH = "9=";
    private static final String MSG_CHECKSUM = "10=";

    public static void main(String[] args) throws Exception {
//        String input = "8=FIX.4.2|9=103|5=D|34=3|49=BANZAI|52=20121105-23:24:42|56=EXEC|11=1352157882577|21=1|38=10000|40=1|54=1|55=MSFT|59=0|10=062";
        String input = "8=FIX.21|9=82|35=B|52=2022-12-29T23:44:37.763078362+03:00|553=0|554=0|100=Bitcoin|101=1|102=1.0|10=137";

        System.out.println(FixProtocol.checkSumGenerator(input));

    }
}