package liar.waitservice.wait.service;

import liar.waitservice.exception.exception.NotExistsRoomIdException;
import liar.waitservice.wait.domain.OnlineHost;
import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.repository.OnlineHostRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OnlineHostService {

    private final OnlineHostRedisRepository onlineHostRedisRepository;


    public void save(WaitRoom waitRoom) {
        OnlineHost find = findByHostIdForSave(waitRoom.getHostId());
        if (find == null) {
            onlineHostRedisRepository.save(new OnlineHost(waitRoom.getHostId(), waitRoom.getId()));
        } else {
            onlineHostRedisRepository.save(new OnlineHost(find.getId(), waitRoom.getId()));
        }
    }

    private OnlineHost findByHostIdForSave(String hostId) {
        return onlineHostRedisRepository.findOnlineHostById(hostId);
    }

    public OnlineHost findByHostIdForSearch(String hostId) {
        return onlineHostRedisRepository.findById(hostId).orElseThrow(NotExistsRoomIdException::new);
    }

}
