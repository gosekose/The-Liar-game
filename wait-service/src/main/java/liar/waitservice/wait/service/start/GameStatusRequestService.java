package liar.waitservice.wait.service.start;

import org.springframework.stereotype.Component;

@Component
public interface GameStatusRequestService<T, R> {

    void saveWaitRoomInfoAtDb(T t);
    void deleteWaitRoomInfoAtCache(R r);

}
