package liar.waitservice.wait.service.start;

import liar.waitservice.wait.MemberDummyInfo;
import liar.waitservice.wait.controller.dto.RequestWaitRoomDto;
import liar.waitservice.wait.controller.dto.PostProcessEndGameDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UpdateWaitRoomCompleteStatusServiceTest extends MemberDummyInfo {

    @Autowired
    DoProcessStartAndEndGameService doProcessStartAndEndGameService;

    @Autowired
    DoProcessStartAndEndGameServiceImpl updateWaitRoomStatusServiceImpl;

    @Test
    @DisplayName("updateWaitRoomStatusService가 호출이 되면 빈으로 등록된 구현체가 작동해야한다.")
    public void isInstanceUpdateWaitRoomStatusServiceImpl() throws Exception {
        //given
        UpdateWaitRoomStatusServiceCaller updateWaitRoomStatusServiceCaller =
                new UpdateWaitRoomStatusServiceCaller
                        <RequestWaitRoomDto, PostProcessEndGameDto>(doProcessStartAndEndGameService);

        //when
        Class clazz = updateWaitRoomStatusServiceCaller.getImpl();

        //then
        boolean isInterface = clazz.isInterface();
        assertThat(isInterface).isFalse();
    }

    private static class UpdateWaitRoomStatusServiceCaller<T, R> {

        private DoProcessStartAndEndGameService doProcessStartAndEndGameService;

        private UpdateWaitRoomStatusServiceCaller(DoProcessStartAndEndGameService doProcessStartAndEndGameService) {
            this.doProcessStartAndEndGameService = doProcessStartAndEndGameService;
        }

        protected Class getImpl() {
            return doProcessStartAndEndGameService.getClass();
        }
    }


}