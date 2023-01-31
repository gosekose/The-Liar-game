package liar.waitservice.wait.service.search;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SearchService<T, R> {
    List<R> searchWaitRoomCond(T request);
}
