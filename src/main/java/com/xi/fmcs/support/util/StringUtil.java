package com.xi.fmcs.support.util;

import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class StringUtil {
    private int seq = 0;

    public String hideNumber(String number) {
        String result = "";
        int endNum = 0; // endNum 위치 까지 숨김
        int startNum = 0; // startNum 위치 부터 숨김

        if (!(number == null || number.isEmpty())) {
            if (number.contains("@")) {
                endNum = number.indexOf("@");
                startNum = 3;
            } else {
                if (number.length() >= 13) {
                    startNum = 4;
                    endNum = 8;
                } else {
                    startNum = 3;
                    endNum = 7;
                }
            }
            result = getResult(number, endNum, startNum);
        }
        return result;
    }

    public String hideName(String name) {
        String result = "";
        int endNum = 0; // endNum 위치 까지 숨김
        int startNum = 1; // startNum 위치 부터 숨김

        if (!(name == null || name.isEmpty())) {
            if (name.length() <= 3) {
                endNum = 2;
            } else {
                endNum = 3;
            }
            result = getResult(name, endNum, startNum);
        }
        return result;
    }

    //hideNumber, hideName 메소드에서 사용함
    private String getResult(String number, int endNumber, int startNumber) {
        return number.substring(0, startNumber)
                + new String(new char[endNumber - startNumber]).replace("\0", "*")
                + number.substring(endNumber);
    }

    public String phoneNumber(String number) {
        String regEx = "(\\d{3})(\\d{3,4})(\\d{4})";
        return number.replaceAll(regEx, "$1-$2-$3");
    }

    public boolean stringDateCheck(String date) {
        boolean result = false;
        result = date.matches("\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[0-1])$");
        return result;
    }

    public boolean stringMailCheck(String email) {
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public String getRandomFolder(SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date()) + String.format("%03d", ++seq % 1000);
    }

    public String getFolderName(SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date());
    }

    public String getFormatSumDate(String date, String hour, String minute) {
        return String.format("%s %s:%s", date, hour, minute);
    }

}
