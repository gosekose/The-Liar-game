package liar.resultservice.result.controller.util;

import liar.resultservice.result.controller.dto.request.MyDetailGameResultRequest;
import liar.resultservice.result.repository.query.myresult.MyDetailGameResultCond;
import org.springframework.stereotype.Component;

@Component
public class RequestMapperFactory {

    public static MyDetailGameResultCond mapper(MyDetailGameResultRequest request) {
        return MyDetailGameResultCond.builder()
                .userId(request.getUserId()).viewLatest(request.getViewLatest())
                .viewOnlyWin(request.getViewOnlyWin()).viewOnlyLose(request.getViewOnlyLose())
                .searchGameName(request.getSearchGameName())
                .build();
    }


}
