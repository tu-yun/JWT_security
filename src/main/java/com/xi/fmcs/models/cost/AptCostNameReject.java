package com.xi.fmcs.models.cost;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class AptCostNameReject {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    public Integer SEQ;

    public String COST_NAME;
}
