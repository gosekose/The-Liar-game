package liar.waitservice.wait.service.search;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SearchService<T, R> {
    List<T> searchWaitRoomByCond(R body);

    Slice<T> searchWaitRoomByCond(R body, Pageable pageable);
}
