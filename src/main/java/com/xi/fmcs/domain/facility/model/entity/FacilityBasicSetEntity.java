package com.xi.fmcs.domain.facility.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "FACILITY_BASIC_SET")
/**
 * 시설 기본 설정
 */
public class FacilityBasicSetEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    public Integer SEQ;

    // 유니크한 값 (아파트 고유번호 apt.seq)
    public Integer APT_SEQ;

    // 노출여부
    public String VIEW_YN;

    /**
     * 연동 구분 (1:웹뷰, 2:API)
     */
    public String LINK_TYPE;

    /**
     * 예약가능일 기본값 7 (0~31)
     */
    public Integer RESERVATION_DAY;

    /**
     * 금액절사단위(1:1, 2:10, 3:100)
     */
    public Integer REFUND_CUT;

    /**
     * 중도접수차감기준 (1:월일수, 2:기준일수)
     */
    public Integer MID_DAY_TYPE;

    // 중도접수차감기준일
    public Integer MID_DAY;

    // 환불불가시간
    public Integer REFUND_HOUR;

    /**
     * 일별차감타입 (1:월일수, 2:기준일수)
     */
    public Integer REFUND_DAY_TYPE;

    // 일별차감기준일
    public Integer REFUND_DAY;

    // 위약금추가차감
    public String REFUND_ADD_CHECK;

    // 추가차감율
    public Integer REFUND_ADD;

    public Integer REG_SEQ;

    public LocalDateTime REG_DATE;

    public Integer MOD_SEQ;

    public LocalDateTime MOD_DATE;

    // 입주민연동주소
    public String SYNC_URL;

    // 회원연동주소
    public String MEM_SYNC_URL;


}