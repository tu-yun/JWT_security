package com.xi.fmcs.domain.test.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name="aptCostMain")
public class TestEntity {
	
    //일렬번호
	@Schema(description = "번호")
    @Id
    private Integer seq;
	
    //APT SEQ
	@Min(value = 1, message = "아파트번호는 {valid.message.min}")
	@Max(value = 30, message = "아파트번호는 {valid.message.max}")
	@Range(min = 1, max = 30, message = "아파트번호는 {valid.message.range}")
	@Positive(message = "아파트번호는 {valid.message.positive}")
	@NotNull(message = "아파트번호는 {valid.message.notNull}")
	@Schema(description = "아파트번호", example = "1", discriminatorMapping = @DiscriminatorMapping(value = "AAA"))
	private Integer aptSeq;
	
    //동
	@NotNull(message = "동은 {valid.message.notNull}")
	@Size(min=3, max=5, message = "동의 {valid.message.size}")
	@Schema(description = "동")
	private String dong;
	
    //호
	@Email(message = "호는 {valid.message.email}")
	@NotEmpty(message = "호는 {valid.message.notEmpty}")
	@Schema(description = "호")
	private String ho;
	
    //관리비 내역일
	@NotBlank(message = "관리비내역일는 {valid.message.notBlank}")
	@Pattern(regexp = "^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "관리비내역일는 {valid.message.date}")
	@Schema(description = "관리비내역일", example = "2023-02")
	private String costDate;
	
    //업로드 목록 동-> 배열
	@Schema(description = "업로드 목록 동")
    @Transient
    private String DONGS;
    
}
