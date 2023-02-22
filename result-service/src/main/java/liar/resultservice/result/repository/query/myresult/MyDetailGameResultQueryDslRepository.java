package liar.resultservice.result.repository.query.myresult;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface MyDetailGameResultQueryDslRepository {

    Slice<MyDetailGameResultDto> fetchMyDetailGameResult(MyDetailGameResultCond cond, Pageable pageable);

}
