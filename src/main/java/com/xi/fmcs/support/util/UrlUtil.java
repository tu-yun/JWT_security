package com.xi.fmcs.support.util;

import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletRequest;

@UtilityClass
public class UrlUtil {

    public static String isNowToHtml(String now, String setHtml, HttpServletRequest request) {
        String r = request.getRequestURI();
        if(r.contains(now)) {
            return setHtml;
        }
        return "";
    }

}
