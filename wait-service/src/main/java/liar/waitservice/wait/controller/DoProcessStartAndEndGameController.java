package liar.waitservice.wait.controller;

import jakarta.validation.Valid;
import liar.waitservice.wait.controller.dto.PostProcessEndGameDto;
import liar.waitservice.wait.controller.dto.RequestWaitRoomDto;
import liar.waitservice.wait.controller.dto.message.SendSuccessProcess;
import liar.waitservice.wait.service.WaitRoomFacadeService;
import liar.waitservice.wait.service.start.DoProcessStartAndEndGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wait-service")
@RequiredArgsConstructor
public class DoProcessStartAndEndGameController {

    private final WaitRoomFacadeService waitRoomFacadeService;

    /**
     * game 시작 전 작업 수행
     * waitRoomRedisRepository에 저장된, waitRoom을
     * waitRoomComplete, WaitRoomCompleteJoinMember 테이블에 저장 (RDBMS)
     */
    @PostMapping("/game/start")
    public ResponseEntity doPreProcessBeforeGameStart(@Valid @RequestBody RequestWaitRoomDto saveRequest) {
        waitRoomFacadeService.doPreProcessBeforeGameStart(saveRequest);
        return ResponseEntity.ok().body(SendSuccessProcess.of(true));
    }

    /**
     * game 시작 후 작업 수행
     * waitRoomRedisRepository에 저장된, waitRoom과
     * joinMemberRedisRepository에 저장된 joinMember를 redis에서 제거
     * waitRoomComplete 테이블의 waitRoomStatus 상태 END로 변경
     */
    @PostMapping("/game/end")
    public ResponseEntity doPostProcessAfterGameEnd(@Valid @RequestBody PostProcessEndGameDto updateRequest) {
        waitRoomFacadeService.doPostProcessAfterGameEnd(updateRequest);
        return ResponseEntity.ok().body(SendSuccessProcess.of(true));
    }

}
