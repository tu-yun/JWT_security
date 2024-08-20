package com.xi.fmcs.models.cost;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class AptCostDetail {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    public Integer SEQ;

    //Cost Main 일렬번호
    public Integer APT_COST_MAIN_SEQ;

    //컬럼 이름
    public String COST_NAME;

    //이번달 값
    public String COST_STR_VAL;

    //이번달 값
    @Column(name = "COST_INT_VAL")
    public Long COST_Integer_VAL;

    //숫자 여부
    @Column(name = "INT_YN")
    public String Integer_YN;

    //컬럼 정렬 순서
    public Integer ORDERNUM;


    /* 이하 db에 없는 */

    //전월 값
    public Long PREV_COST_Integer_VAL;

    //이번달 값 String
    public String TEXT_COST_Integer_VAL;

    //전월 값 String
    public String TEXT_PREV_COST_Integer_VAL;

    //평균 값 String
    public String TEXT_SVG_COST_Integer_VAL;

    //전월대비 증감
    public String TEXT_ADD_Integer_VAL;

    //동일평수 평규 당월부과금액
    public Long AVG_AMT;

    //평수
    public Long APT_SIZE;

}
