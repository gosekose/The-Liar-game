package liar.gamemvcservice.exception.exception;

import liar.gamemvcservice.exception.type.ExceptionCode;
import liar.gamemvcservice.exception.type.ExceptionMessage;

public class NotSatisfiedMinJoinMembers extends CommonException {
    public NotSatisfiedMinJoinMembers() {super(ExceptionCode.BAD_REQUEST, ExceptionMessage.NOT_SATISFIED_MIN_JOIN_MEMBERS);}
}
