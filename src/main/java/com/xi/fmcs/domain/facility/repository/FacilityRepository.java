package com.xi.fmcs.domain.facility.repository;

import com.xi.fmcs.domain.facility.model.entity.FacilityBasicSetEntity;
import com.xi.fmcs.domain.facility.model.interfaces.FacilityBasicSetInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface FacilityRepository extends JpaRepository<FacilityBasicSetEntity,Integer> {

    @Procedure("XI_SP_FMCS_ADMIN_FACILITY_BASIC_SET_R")
    List<FacilityBasicSetInterface> getFacilityBasicSet(
            @Param("@APT_SEQ") Integer aptSeq
    );

}
