package liar.waitservice.wait.service.search;

import java.util.List;

public interface SearchService<T, R> {
    List<R> searchWaitRoomCondition(T request);
}
