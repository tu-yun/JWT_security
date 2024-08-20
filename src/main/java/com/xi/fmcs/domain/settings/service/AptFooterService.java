package com.xi.fmcs.domain.settings.service;

import com.xi.fmcs.domain.settings.model.AptFooterDto;
import com.xi.fmcs.domain.settings.model.AptFooterSaveRequestDto;
import com.xi.fmcs.domain.settings.repository.AptFooterRepository;
import com.xi.fmcs.support.model.Result;
import com.xi.fmcs.support.util.MngUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AptFooterService {

    private final AptFooterRepository aptFooterRepository;

    /// 내단지 정보
    public Result<AptFooterDto> getAptFooter(int aptSeq) {
        return Result.<AptFooterDto>builder()
                .result(aptFooterRepository.getAptFooter(aptSeq))
                .build();
    }

    /// 내 단지 Footer 저장
    @Transactional
    public Result<String> saveAptFooter(AptFooterSaveRequestDto aptFooterSaveRequest, int regSeq) {
        String msg = "";
        if(aptFooterSaveRequest.getSeq() > 0) {
            aptFooterRepository.updateAptFooter(aptFooterSaveRequest, regSeq);
            msg = MngUtil.message("FT004");//내단지 정보가 수정되었습니다.
        } else {
            aptFooterRepository.insertAptFooter(aptFooterSaveRequest, regSeq);
            msg = MngUtil.message("FT003");//내단지 정보가 등록되었습니다.
        }

        return Result.<String>builder()
                .stateMessage(msg)
                .build();
    }
}
