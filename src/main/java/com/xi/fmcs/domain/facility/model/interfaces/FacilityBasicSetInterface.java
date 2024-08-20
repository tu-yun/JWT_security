package com.xi.fmcs.domain.facility.model.interfaces;

public interface FacilityBasicSetInterface {

    Integer getSeq();
    Integer getAptSeq();
    String getViewYn();
    String getLinkType();
    Integer getReservationDay();
    Integer getRefundCut();
    Integer getMidDayType();
    Integer getMidDay();
    Integer getRefundHour();
    Integer getRefundDayType();
    Integer getRefundDay();
    String getRefundAddCheck();
    Integer getRefundAdd();
    String getSyncUrl();
    String getMemSyncUrl();

}
