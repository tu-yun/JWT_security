package com.xi.fmcs.support.enums;

import java.util.Arrays;

public enum VoteAnswerTypeEnum {
    MULTIPLE(1, "객관식"),
    ESSAY(2, "주관식");

    private final int code;
    private final String description;

    VoteAnswerTypeEnum(int value, String strVal) {
        this.code = value;
        this.description = strVal;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static VoteAnswerTypeEnum valueOf(int code) {
        return Arrays.stream(values())
                .filter(val -> val.getCode() == code)
                .findFirst()
                .get();
    }

    public static String getDescriptionByCode(int code) {
        return Arrays.stream(values())
                .filter(val -> val.getCode() == code)
                .findFirst()
                .get().getDescription();
    }

    public static String getCodeByDescription(String description) {
        return Arrays.stream(values())
                .filter(val -> val.getDescription() == description)
                .findFirst()
                .get().getDescription();
    }
}
