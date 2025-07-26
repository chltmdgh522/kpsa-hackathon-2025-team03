package game3.domain.quiz.application.service;

import game3.domain.quiz.presentation.dto.res.record.AIRecordRes;
import game3.domain.quiz.presentation.dto.res.record.EndingRes;
import game3.domain.quiz.presentation.dto.res.record.RecordListRes;
import game3.domain.quiz.presentation.dto.res.record.personal.RecordPersonalRes;

import java.util.List;

public interface RecordService {


    List<RecordListRes> recordList(String userId);

    RecordPersonalRes recordPersonal(String userId, Long quizRecordId);


    List<AIRecordRes> aiRecordPersonal(String userId, Long quizRecordId);


    EndingRes recordEnding (String userId, Long quizRecordId);

}
