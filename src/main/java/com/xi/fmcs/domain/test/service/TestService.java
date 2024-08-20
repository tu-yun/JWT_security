package com.xi.fmcs.domain.test.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.domain.test.model.TestEntity;
import com.xi.fmcs.domain.test.repository.TestRepository;

@Service
public class TestService {
	
	@Autowired
	private TestRepository testRepo;
	
	public Result<List<TestEntity>> costList(String cDate, int aptSeq) {		
		return Result.<List<TestEntity>>builder()
				.result(testRepo.GetCostMainList(cDate, aptSeq))
				.stateCode("423")
				.stateMessage("성공")
				.build();
	}
	
	public Result<List<TestEntity>> costList2(String cDate, int aptSeq) {	
		testRepo.findAll();
		return Result.<List<TestEntity>>builder()
				.result(testRepo.GetCostMainList(cDate, aptSeq))
				.build();
	}
	
}
