package liar.resultservice.result.service.myresult;

import liar.resultservice.result.repository.query.myresult.MyGameResultDetailInfoCond;
import liar.resultservice.result.repository.query.myresult.MyGameResultDetailInfoDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public interface MyGameResultPolicy {

    Slice<MyGameResultDetailInfoDto> fetchMyGameResultInfoByCond(MyGameResultDetailInfoCond myGameResultInfoCond, Pageable pageable);

}
