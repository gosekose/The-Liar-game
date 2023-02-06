package liar.waitservice.exception.exception;

import liar.waitservice.exception.type.ExceptionCode;
import liar.waitservice.exception.type.ExceptionMessage;

public class NotSatisfiedMinJoinMembers extends CommonException {
    public NotSatisfiedMinJoinMembers() {super(ExceptionCode.BAD_REQUEST, ExceptionMessage.NOT_SATISFIED_MIN_JOIN_MEMBERS);}
}
