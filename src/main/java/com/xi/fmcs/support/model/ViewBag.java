package com.xi.fmcs.support.model;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ViewBag {
	
	@Schema(description = "총수량")
    private Integer totalCount;
	
	@Schema(description = "현재페이지")
    private Integer currentPage;
	
	@Schema(description = "페이지사이즈")
    private Integer pageSize;
	
	@Schema(description = "검색조건")
    private Map<String, Object> search;

}
