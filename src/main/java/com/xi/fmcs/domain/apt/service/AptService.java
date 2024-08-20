package com.xi.fmcs.domain.apt.service;

import com.xi.fmcs.domain.apt.repository.AptRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class AptService {

    private final AptRepository aptRepository;
    private final HttpSession session;

    public AptService(AptRepository aptRepository, HttpSession session) {
        this.aptRepository = aptRepository;
        this.session = session;
    }

//    public List<Map<String,Object>> getAptMngGsList (
//            String cmpSchVal, String aptSchVal,
//            int doType, int pageNum, int pageSize
//    ) {
////        return aptRepository.findAll();
//        return aptRepository.getAptMngGsList(cmpSchVal,aptSchVal,doType,pageNum,pageSize);
//    }
//
//    public List<Map<String,Object>> GetAptMngList (
//            String aptSchVal,
//            int doType, int pageNum, int pageSize
//    ) {
//        AdminMemberEntity adminMemberEntity = (AdminMemberEntity) session.getAttribute("adminSession");
//        long cmpSeq = 0;
//        if(adminMemberEntity !=null) {
//            cmpSeq = adminMemberEntity.getCompanySeq();
//        }
//        return aptRepository.getAptMngList(cmpSeq, aptSchVal, doType, pageNum, pageSize);
//    }
//
//    @Transactional
//    public AptEntity saveApt(AptEntity apt) {
//        return aptRepository.save(apt);
//    }
//
//    public List<AptEntity> getApt () {
//        return aptRepository.findAll();
//    }

}
