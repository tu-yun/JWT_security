package com.xi.fmcs.models.sendAuth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class EmailAuth {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    public Integer SEQ;

    public String EMAIL;

    //인증번호
    public String AUTH;

    @CreatedDate
    public LocalDateTime REG_DATE;
}
