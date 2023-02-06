package liar.gameservice.exception.exception;

import liar.gameservice.exception.type.ExceptionCode;
import liar.gameservice.exception.type.ExceptionMessage;

public class NotSatisfiedMinJoinMembers extends CommonException {
    public NotSatisfiedMinJoinMembers() {super(ExceptionCode.BAD_REQUEST, ExceptionMessage.NOT_SATISFIED_MIN_JOIN_MEMBERS);}
}
