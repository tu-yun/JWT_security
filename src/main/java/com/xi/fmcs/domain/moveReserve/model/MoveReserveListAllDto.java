package com.xi.fmcs.domain.moveReserve.model;

import lombok.Data;

import java.time.LocalDateTime;

//XI_SP_FMCS_ADMIN_APT_MOVE_RESERVE_L_ALL
@Data
public class MoveReserveListAllDto {

    private int rowNum;
    private int seq;
    private int aptSeq;
    private String dong;

    private String ho;
    private int elvtrNo;
    private String moveDate;
    private String moveTime;
    private int aptMoveTimeSetSeq;
    private int moveType;
    private String regName;

    private String isAdmin;

    private String regTel;
    private String moveTypeName;
    private String name;
    private String nickName;
    private LocalDateTime regDate;
    private int totalcnt;

}
