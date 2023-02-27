package liar.resultservice.result.controller.util;

import liar.resultservice.result.controller.dto.request.MyDetailGameResultRequest;
import liar.resultservice.result.controller.dto.request.SaveResultRequest;
import liar.resultservice.result.repository.query.myresult.MyDetailGameResultCond;
import liar.resultservice.result.service.dto.SaveResultDto;
import org.springframework.stereotype.Component;

@Component
public class RequestMapperFactory {

    public static MyDetailGameResultCond mapper(MyDetailGameResultRequest request) {
        return MyDetailGameResultCond.builder()
                .userId(request.getUserId())
                .viewLatest(request.getViewLatest())
                .viewOnlyWin(request.getViewOnlyWin())
                .viewOnlyLose(request.getViewOnlyLose())
                .searchGameName(request.getSearchGameName())
                .build();
    }

    public static SaveResultDto mapper(SaveResultRequest request) {
        return SaveResultDto.builder()
                .gameId(request.getGameId())
                .winner(request.getWinner())
                .playersInfo(request.getPlayersInfo())
                .roomId(request.getRoomId())
                .gameName(request.getGameName())
                .hostId(request.getHostId())
                .topicId(request.getTopicId())
                .totalUserCnt(request.getTotalUserCnt())
                .votedResults(request.getVotedResults())
                .build();
    }

}
