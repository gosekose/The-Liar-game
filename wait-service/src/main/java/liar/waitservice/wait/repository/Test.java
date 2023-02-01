package liar.waitservice.wait.repository;

import liar.waitservice.wait.controller.dto.CreateWaitRoomDto;
import liar.waitservice.wait.domain.WaitRoom;

import java.time.LocalDateTime;

public class Test {

    public static void main(String[] args) throws ClassNotFoundException {

        CreateWaitRoomDto createWaitRoomDto = new CreateWaitRoomDto("124", "333", 3);
        System.out.println("createWaitRoomDto = " + createWaitRoomDto.getClass().getSimpleName());

        Class<?> findClass = Class.forName(createWaitRoomDto.getClass().getName());
        System.out.println("findClass.getTypeName(); = " + findClass.getTypeName());

        
        String a = "rr";
        boolean test = a instanceof String;
        System.out.println("a instanceof String = " + a);

        Object ab = new Object();
        Long abr = 13L;
        String s = "sd" + abr;

        long abc = 3L;

        String af = abc + "sd";

        boolean b = createWaitRoomDto instanceof Object;
        System.out.println("b = " + b);


    }

}
