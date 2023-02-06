package liar.waitservice.wait.service.start;

import org.springframework.stereotype.Component;

@Component
public interface DoProcessStartAndEndGameService<T, R> {

    /**
     * game 시작 전 작업 수행
     * waitRoomRedisRepository에 저장된, waitRoom을
     * waitRoomComplete, WaitRoomCompleteJoinMember 테이블에 저장 (RDBMS)
     */
    void doPreProcessBeforeGameStart(T t);

    /**
     * game 시작 후 작업 수행
     * waitRoomRedisRepository에 저장된, waitRoom과
     * joinMemberRedisRepository에 저장된 joinMember를 redis에서 제거
     * waitRoomComplete 테이블의 waitRoomStatus 상태 END로 변경
     */
    void doPostProcessAfterGameEnd(R r);

}
