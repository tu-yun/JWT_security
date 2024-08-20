package com.xi.fmcs.domain.member.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberRDto {
    @Schema(description = "회원 고유번호")
    private int seq;

    @Schema(description = "회원 유니크ID")
    private String uniqueId;

    private int aptSeq;

    private String aptDong;

    private String aptHo;

    private String name;

    private String nickname;

    private String phone;

    @Schema(description = "0:없음, 1:세대원, 2:세대주")
    private int householderType;

    @Schema(description = "1:자가, 2:임대, 3:소유주")
    private int residenceType;

    private String sex;

    @Schema(description = "1:승인대기, 2:승인회원, 3:미승인회원, 4:전출회원")
    private int status;

    @Schema(description = "투표 유무")
    private String voteYn;

    @JsonIgnore
    private String dongMngYn;

    private String birthday;

    private String rfCardNo;

    private String imageUrl;

}
