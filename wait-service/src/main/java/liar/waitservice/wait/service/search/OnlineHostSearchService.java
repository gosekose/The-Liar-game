package liar.waitservice.wait.service.search;

import liar.waitservice.other.MemberService;
import liar.waitservice.wait.service.OnlineHostService;
import liar.waitservice.wait.service.WaitRoomService;
import liar.waitservice.wait.service.search.dto.WaitRoomViewsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OnlineHostSearchService implements SearchService {

    private final OnlineHostService onlineHostService;
    private final WaitRoomService waitRoomService;
    private final MemberService memberService;

    @Override
    public List searchWaitRoomCond(Object request) {
        return memberService.findByUsername((String) request)
                .stream().map(f -> onlineHostService.findByHostIdForSearch(f.getUserId()))
                .map(f -> waitRoomService.findRoomId(f.getWaitRoomId()))
                .map(WaitRoomViewsDto::new)
                .collect(Collectors.toList());
    }
}
