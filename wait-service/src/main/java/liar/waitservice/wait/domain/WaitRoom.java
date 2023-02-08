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

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

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

    private List<String> members = new LinkedList<>();

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
    }

    public static WaitRoom of(CreateWaitRoomDto createWaitRoomDto, String userName) {
        return new WaitRoom(createWaitRoomDto, userName);
    }

    /**
     * 대기방 인원이 여유가 있고, 요청이 호스트가 아니라면 회원 추가
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
     * 대기방에 있는 유저 나가기
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
     * 요청 유저 아이가 방의 호스트와 같은지 파악
     */
    public boolean isHost(String userId) {
        return userId.equals(this.hostId);
    }

    /**
     * 대기방 만석 파악
     */
    private boolean isNotFullMembers() {
        return members.size() < limitMembers;
    }

}
