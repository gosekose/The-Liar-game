package liar.resultservice.result.controller;

import liar.resultservice.result.controller.dto.message.SendSuccessBody;
import liar.resultservice.result.controller.interceptor.anno.MyDetailGameResult;
import liar.resultservice.result.repository.query.myresult.MyDetailGameResultCond;
import liar.resultservice.result.service.ResultFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/result-service/result")
public class FetchResultController {

    private final ResultFacadeService resultFacadeService;

    @GetMapping("/rank")
    public ResponseEntity fetchPlayerRank(Pageable pageable) {
        return ResponseEntity.ok()
                .body(SendSuccessBody.of(resultFacadeService.fetchPlayerRank(pageable)));
    }

    @GetMapping("/{userId}")
    @MyDetailGameResult
    public ResponseEntity fetchMyDetailGameResult(@PathVariable String userId,
                                                  @RequestBody MyDetailGameResultCond cond,
                                                  Pageable pageable) {
        return ResponseEntity.ok()
                .body(SendSuccessBody.of(resultFacadeService.fetchMyDetailGameResult(cond, pageable)));
    }

}
