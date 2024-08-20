package com.xi.fmcs.support.enums;

import java.util.Arrays;

public enum VoteAnswerItemTypeEnum {
    TITLE(1, "제목"),
    TITLE_CONTENT(2, "제목/설명"),
    IMAGE_TITLE(3, "이미지/제목"),
    IMAGE_TITLE_CONTENT(4, "이미지/제목/설명");

    private final int code;
    private final String description;

    VoteAnswerItemTypeEnum(int value, String strVal) {
        this.code = value;
        this.description = strVal;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static VoteAnswerItemTypeEnum valueOf(int code) {
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
