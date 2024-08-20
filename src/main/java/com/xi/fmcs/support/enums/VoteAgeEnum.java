package com.xi.fmcs.support.enums;

import java.util.Arrays;

public enum VoteAgeEnum {
    ALL(0,"전체"),
    TEENAGER(1, "10대"),
    TWENTIES(2,"20대"),
    THIRTIES(3,"30대"),
    FORTIES(4,"40대"),
    FIFTIES(5,"50대"),
    ELDER(6,"60대 이상");

    private final int code;
    private final String description;

    VoteAgeEnum(int value, String strVal) {
        this.code = value;
        this.description = strVal;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static VoteAgeEnum valueOf(int code) {
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
