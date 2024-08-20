package com.xi.fmcs.domain.test.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.xi.fmcs.domain.test.model.TestEntity;

public interface TestRepository extends JpaRepository<TestEntity, Integer>{

	@Procedure("XI_SP_FMCS_ADMIN_APT_COST_MAIN_L")
	List<TestEntity> GetCostMainList(
			@Param("@COST_DATE") String cDate,
			@Param("@APT_SEQ") int aptSeq
	);
	
}
