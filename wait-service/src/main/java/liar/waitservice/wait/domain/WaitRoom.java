package liar.waitservice.wait.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import liar.waitservice.wait.controller.dto.CreateWaitRoomDto;
import liar.waitservice.wait.domain.utils.BaseRedisTemplateEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.time.LocalDateTime.now;

@Getter
@RedisHash("WaitRoom")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitRoom extends BaseRedisTemplateEntity {

    @Id
    private String id;

    @Indexed
    private String roomName;

    @Indexed
    private String hostId;

    @Indexed
    private String hostName;

    private int limitMembers;
    private boolean waiting;

    private List<String> members = new CopyOnWriteArrayList<>();

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime modifiedAt;

    protected WaitRoom (CreateWaitRoomDto roomDto, String userName) {
        id = UUID.randomUUID().toString();
        roomName = roomDto.getRoomName();
        hostId = roomDto.getUserId();
        hostName = userName;
        limitMembers = roomDto.getLimitMembers();
        createdAt = now();
        modifiedAt = now();
        members.add(hostId);
        waiting = true;
    }

    public static WaitRoom of(CreateWaitRoomDto createWaitRoomDto, String userName) {
        return new WaitRoom(createWaitRoomDto, userName);
    }

    /**
     * ????????? ????????? ????????? ??????, ????????? ???????????? ???????????? ?????? ??????
     */
    public boolean joinMembers(String userId) {

        if (isNotFullMembers() && !isHost(userId)) {
            members.add(userId);
            modifiedAt = now();
            return true;
        }
        return false;
    }

    /**
     * ???????????? ?????? ?????? ?????????
     */
    public boolean leaveMember(String userId) {
        if (!isHost(userId)) {
            int size = members.size();
            members.remove(userId);

            if (size != members.size()) {
                modifiedAt = now();
                return true;
            }
        }
        return false;
    }

    /**
     * ?????? ?????? ????????? ?????? ???????????? ????????? ??????
     */
    public boolean isHost(String userId) {
        return userId.equals(this.hostId);
    }

    public void updateWaiting() { this.waiting = false; }

    /**
     * ????????? ?????? ??????
     */
    private boolean isNotFullMembers() {
        return members.size() < limitMembers;
    }

}
