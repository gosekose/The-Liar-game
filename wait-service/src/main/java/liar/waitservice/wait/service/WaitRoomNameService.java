package liar.waitservice.wait.service;

import liar.waitservice.exception.exception.NotExistsRoomIdException;
import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.domain.WaitRoomName;
import liar.waitservice.wait.repository.WaitRoomNameRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class WaitRoomNameService {

    private final WaitRoomNameRedisRepository waitRoomNameRedisRepository;

    public WaitRoomName findByIdForSearch(String roomName) {
        return waitRoomNameRedisRepository.findById(roomName).orElseThrow(NotExistsRoomIdException::new);
    }

    public WaitRoomName findByIdForSave(String roomName) {
        return waitRoomNameRedisRepository.findWaitRoomNameById(roomName);
    }


    /**
     * 이미 저장된 waitRoomName이 있다면, linkedList에 추가하고, 없다면 새로 생성하여 저장
     */
    public void save(WaitRoom waitRoom) {
        WaitRoomName waitRoomName = findByIdForSave(waitRoom.getRoomName());

        if (waitRoomName == null) {
            waitRoomNameRedisRepository.save(new WaitRoomName(waitRoom));
        } else {
            waitRoomName.addWaitRoomIds(waitRoom.getId());
            waitRoomNameRedisRepository.save(waitRoomName);
        }
    }
}
