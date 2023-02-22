package liar.resultservice.result.service.myresult;

import liar.resultservice.result.repository.query.myresult.MyDetailGameResultCond;
import liar.resultservice.result.repository.query.myresult.MyDetailGameResultDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public interface MyGameResultPolicy {

    Slice<MyDetailGameResultDto> fetchMyGameResultInfoByCond(MyDetailGameResultCond myGameResultInfoCond, Pageable pageable);

}
