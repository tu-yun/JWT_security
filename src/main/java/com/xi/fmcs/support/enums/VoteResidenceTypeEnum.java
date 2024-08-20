package com.xi.fmcs.support.enums;

import java.util.Arrays;

public enum VoteResidenceTypeEnum {
    OWN(1, "자가"),
    RENT(2, "임대"),
    UNRESIDENTIAL_OWNER(3, "소유주(미거주)");

    private final int code;
    private final String description;

    VoteResidenceTypeEnum(int value, String strVal) {
        this.code = value;
        this.description = strVal;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static VoteResidenceTypeEnum valueOf(int code) {
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
