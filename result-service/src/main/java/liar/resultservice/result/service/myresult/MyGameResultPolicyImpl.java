package liar.resultservice.result.service.myresult;

import liar.resultservice.result.repository.query.myresult.MyDetailGameResultCond;
import liar.resultservice.result.repository.query.myresult.MyDetailGameResultDto;
import liar.resultservice.result.repository.query.myresult.MyDetailGameResultQueryDslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyGameResultPolicyImpl implements MyGameResultPolicy {
    private final MyDetailGameResultQueryDslRepository repository;

    @Override
    public Slice<MyDetailGameResultDto> fetchMyGameResultInfoByCond(MyDetailGameResultCond cond, Pageable pageable) {
        return repository.fetchMyDetailGameResult(cond, pageable);
    }
}
