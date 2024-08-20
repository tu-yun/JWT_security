package com.xi.fmcs.domain.vote.repository;

import com.xi.fmcs.support.util.LogUtil;
import com.xi.fmcs.domain.file.model.FileInfoDetailDto;
import com.xi.fmcs.domain.vote.model.*;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class VoteRepository {

    private final SqlSessionTemplate sqlSession;
    private final String mapperName = this.getClass().getName();

    //투표/설문 목록 조회
    public List<VoteListResponseDto> getVoteList(VoteListRequestDto voteListRequest, int page, int pageSize) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_VOTE_L";

        try {
            params.put("@APT_SEQ", voteListRequest.getAptSeq());
            params.put("@SRCH_TXT", voteListRequest.getSearchText());
            params.put("@SRCH_TYPE", voteListRequest.getSearchType());
            params.put("@SRCH_START_DT", voteListRequest.getSearchStartDate());
            params.put("@SRCH_END_DT", voteListRequest.getSearchEndDate());
            params.put("@SRCH_ST", voteListRequest.getSearchSt());
            params.put("@PAGE_NUM", page);
            params.put("@PAGE_SIZE", pageSize);

            return sqlSession.selectList(mapperName + "." + spName, params);

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //투표(설문) 상세
    public VoteDetailDto getVoteDetail(int voteInfoSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_VOTE_R";

        try {
            params.put("@SEQ", voteInfoSeq);
            return sqlSession.selectOne(mapperName + "." + spName, params);

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //질문 목록(투표/설문 수정 , 설문 상세)
    public List<VoteQuestionResponseDto> getVoteQuestionList(int voteInfoSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_VOTE_QUESTION_L";

        try {
            params.put("@VOTE_INFO_SEQ", voteInfoSeq);

            return sqlSession.selectList(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //투표 질문/답안 항목 목록(상세)
    public List<VoteQuestionAnswerDto> getVoteQuestAnsList1(int voteInfoSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_VOTE_QUESTION_ANSWER1_L";

        try {
            params.put("@VOTE_INFO_SEQ", voteInfoSeq);

            return sqlSession.selectList(mapperName + "." + spName, params);

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //투표 질문/답안 항목 목록(질문/답안 수정)
    public List<VoteQuestionAnswerDto> getVoteQuestAnsList2(int answerType, int voteQuestionSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_VOTE_QUESTION_ANSWER2_L";

        try {
            params.put("@ANSWER_TYPE", answerType);
            params.put("@VOTE_QUESTION_SEQ", voteQuestionSeq);

            return sqlSession.selectList(mapperName + "." + spName, params);

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //투표 인명부(참여자)목록
    public List<VoteMemberDto> getVoteMemberList(int voteInfoSeq, int voteDiv, int page) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_VOTE_MEMBER_L";

        try {
            params.put("@VOTE_INFO_SEQ", voteInfoSeq);
            params.put("@VOTE_DIV", voteDiv);
            params.put("@PAGE_NUM", page);

            return sqlSession.selectList(mapperName + "." + spName, params);

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //설문 참여자 목록
    public List<VoteMemberDto> getVoteMemberList2(int voteInfoSeq, int page) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_VOTE_MEMBER2_L";

        try {
            params.put("@VOTE_INFO_SEQ", voteInfoSeq);
            params.put("@PAGE_NUM", page);

            return sqlSession.selectList(mapperName + "." + spName, params);

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //투표/설문 기본정보 수정
    public void updateVoteInfo(VoteInfoSaveRequestDto voteInfoSaveRequest, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_VOTE_INFO_U";

        try {
            String[] voteAgeArr = voteInfoSaveRequest.getVoteAge();
            String voteAge = null;
            if(voteAgeArr != null) {
                voteAge = String.join(",", voteAgeArr);
            }
            params.put("@SEQ", voteInfoSaveRequest.getVoteInfoSeq());
            params.put("@VOTE_GENDER", voteInfoSaveRequest.getVoteGender());
            params.put("@VOTE_AGE", voteAge);
            params.put("@TITLE", voteInfoSaveRequest.getVoteTitle());
            params.put("@CONTENTS", voteInfoSaveRequest.getContents());
            params.put("@START_DATE", voteInfoSaveRequest.getStartDate());
            params.put("@END_DATE", voteInfoSaveRequest.getEndDate());
            params.put("@ADMIN_SEQ", regSeq);

            sqlSession.update(mapperName + "." + spName, params);

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //투표/설문 기본정보 등록
    public int insertVoteInfo(VoteInfoSaveRequestDto voteInfoSaveRequest, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_VOTE_INFO_C";

        try {
            String voteAge = null;
            if (voteInfoSaveRequest.getVoteAge() != null) {
                voteAge = String.join(",", voteInfoSaveRequest.getVoteAge());
            }

            params.put("@APT_SEQ", voteInfoSaveRequest.getAptSeq());
            params.put("@VOTE_TYPE", voteInfoSaveRequest.getVoteType());
            params.put("@VOTE_GENDER", voteInfoSaveRequest.getVoteGender());
            params.put("@VOTE_AGE", String.join(",", voteAge));
            params.put("@TITLE", voteInfoSaveRequest.getVoteTitle());
            params.put("@CONTENTS", voteInfoSaveRequest.getContents());
            params.put("@START_DATE", voteInfoSaveRequest.getStartDate());
            params.put("@END_DATE", voteInfoSaveRequest.getEndDate());
            params.put("@ADMIN_SEQ", regSeq);

            System.out.println("VOTE_AGE : " + voteAge);

            sqlSession.insert(mapperName + "." + spName, params);
            return (int) params.get("@SEQ");

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //동 저장(설문)
    public void setVoteDong(int voteInfoSeq, String dongStr) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_VOTE_DONG_C";

        try {
            params.put("@SEQ", voteInfoSeq);
            params.put("@DONG_STR", dongStr);

            sqlSession.insert(mapperName + "." + spName, params);

        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //투표/설문 삭제
    public int deleteVoteInfo(int voteInfoSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_VOTE_D";

        try {
            params.put("@SEQ", voteInfoSeq);

            sqlSession.delete(mapperName + "." + spName, params);
            return (int) params.get("@RET_VAL");
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //투표(설문) 질문 등록 : @VOTE_QUESTION_SEQ == 0 이면 insert 아니면 업데이트
    public int insertVoteQuestion(VoteQuestionRequestDto voteQuestionRequest, int regSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_VOTE_QUESTION_C";

        try {
            params.put("@VOTE_INFO_SEQ", voteQuestionRequest.getVoteInfoSeq());
            params.put("@QUESTION_TITLE", voteQuestionRequest.getQuestionTitle());
            params.put("@ANSWER_TYPE", voteQuestionRequest.getAnswerType());
            params.put("@VOTE_CHOICE_CNT", voteQuestionRequest.getVoteChoiceCnt());
            params.put("@ANSWER_ITEM_TYPE", voteQuestionRequest.getAnswerItemType());
            params.put("@ADMIN_SEQ", regSeq);
            params.put("@VOTE_QUESTION_SEQ", voteQuestionRequest.getSeq());

            sqlSession.insert(mapperName + "." + spName, params);
            return (int) params.get("@SEQ");
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //투표(설문) 질문/답 삭제
    public int delVoteQuestion(int voteQuestionSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_VOTE_QUESTION_D";
        try {
            params.put("@VOTE_QUESTION_SEQ", voteQuestionSeq);

            sqlSession.delete(mapperName + "." + spName, params);

            return (int) params.get("@RET_VAL"); //1이면 성공, -999면 실패(대기중일경우만 삭제 가능)
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //투표(설문) 답 삭제
    public int delVoteAnswer(int voteInfoSeq, int ansSeq) {
        Map<String, Object> params = new HashMap<String, Object>();
        String spName = "XI_SP_FMCS_ADMIN_VOTE_ANSWER_D";
        try {
            params.put("@VOTE_INFO_SEQ", voteInfoSeq);
            params.put("@ANSWER_SEQ", ansSeq);

            sqlSession.delete(mapperName + "." + spName, params);

            return (int) params.get("@RET_VAL"); //1이면 성공, -999면 실패(대기중일경우만 삭제 가능)
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //답변유형 저장
    public List<VoteAnswerResponseDto> insertVoteAnswer(int seq, List<VoteAnswerRequestDto> answers, int regSeq) {
        Map<String, Object> params = null;
        String spName = "XI_SP_FMCS_ADMIN_VOTE_ANSWER_C";
        try {
            List<VoteAnswerResponseDto> voteAnswerList = new ArrayList<>();
            for (VoteAnswerRequestDto answer : answers) {
                params = new HashMap<String, Object>();
                params.put("@VOTE_QUESTION_SEQ", seq);
                params.put("@ANSWER_NO", answer.getAnswerNo());
                params.put("@ANSWER_TITLE", answer.getAnswerTitle());
                params.put("@ANSWER_CONTENTS", answer.getAnswerContents());
                params.put("@ADMIN_SEQ", regSeq);
                params.put("@ANSWER_SEQ", answer.getVoteAnswerSeq());

                sqlSession.insert(mapperName + "." + spName, params);
                int resultSeq = (int) params.get("@SEQ");
                VoteAnswerResponseDto ans = new VoteAnswerResponseDto();
                ans.setVoteAnswerSeq(resultSeq);
                ans.setAnswerNo(answer.getAnswerNo());
                voteAnswerList.add(ans);
            }
            return voteAnswerList;
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //투표/설문 질문 파일 목록
    public List<FileInfoDetailDto> getVoteQuestFileInfo(int voteQuestionSeq) {
        Map<String, Object> params = null;
        String spName = "XI_SP_FMCS_ADMIN_VOTE_QUESTION_FILE_L";
        try {
            params = new HashMap<String, Object>();
            params.put("@VOTE_QUESTION_SEQ", voteQuestionSeq);

            return sqlSession.selectList(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //투표/설문 결과 목록(결과 팝업)
    public List<VoteQuestionAnswerDto> getVoteQuestAnsResult(int answerType, int voteInfoSeq, int voteQuestionSeq) {
        Map<String, Object> params = null;
        String spName = "XI_SP_FMCS_ADMIN_VOTE_QUESTION_RESULT_L";
        try {
            params = new HashMap<String, Object>();
            params.put("@ANSWER_TYPE", answerType);
            params.put("@VOTE_INFO_SEQ", voteInfoSeq);
            params.put("@VOTE_QUESTION_SEQ", voteQuestionSeq);

            return sqlSession.selectList(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }

    //투표인명부 저장(결과 팝업)
    public int insertVoteMember(VoteMemberSaveRequestDto voteMemberSaveRequest) {
        Map<String, Object> params = null;
        String spName = "XI_SP_FMCS_ADMIN_VOTE_MEMBER_C";
        try {
            params = new HashMap<String, Object>();
            params.put("@APT_SEQ", voteMemberSaveRequest.getAptSeq());
            params.put("@VOTE_INFO_SEQ", voteMemberSaveRequest.getVoteInfoSeq());
            params.put("@VOTE_RESIDENCE", String.join(",",voteMemberSaveRequest.getVoteResidenceArr()));
            params.put("@VOTE_MEMBER_AGE_DATE", voteMemberSaveRequest.getVoteMemberAgeDate());
            params.put("@VOTE_MEMBER_AUTH_DATE", voteMemberSaveRequest.getVoteMemberAuthDate());
            params.put("@DONG_STR", String.join(",",voteMemberSaveRequest.getDong()));

            sqlSession.insert(mapperName + "." + spName, params);
            return (int) params.get("@RET_VAL");
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            throw e;
        }
    }

    //투표 인명부(참여자)/설문참여자 목록(엑셀)
    public List<Map<String,Object>> getVoteMemberExcelList(int voteInfoSeq, int downType) {
        Map<String, Object> params = null;
        String spName = "XI_SP_FMCS_ADMIN_VOTE_MEMBER_EXCEL_L";
        try {
            params = new HashMap<String, Object>();
            params.put("@VOTE_INFO_SEQ", voteInfoSeq);
            params.put("@VOTE_TYPE", downType);

            return sqlSession.selectList(mapperName + "." + spName, params);
        } catch (Exception e) {
            LogUtil.writeLog(spName, params, e.getMessage());
            return null;
        }
    }
}
