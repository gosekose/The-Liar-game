package liar.gameservice.other.waitroomcomplete;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class WaitRoomCompleteService {

    private final WaitRoomCompleteRepository waitRoomCompleteRepository;

}
