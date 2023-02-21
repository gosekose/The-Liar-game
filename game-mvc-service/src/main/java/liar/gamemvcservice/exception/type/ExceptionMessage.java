package liar.gamemvcservice.exception.type;

public class ExceptionMessage {
    public static final String USER_NOT_FOUND = "존재하지 않는 회원입니다.";
    public static final String USER_NOT_REFRESHTOKEN = "존재하지 않는 refreshToken 입니다,";
    public static final String BINDING_INVALID = "유효하지 않은 요청입니다.";
    public static final String BADREQUEST = "유효하지 않은 요청입니다.";
    public static final String INTERNAL_SERVER_ERROR = "일시적인 서버 에러입니다.";
    public static final String NOT_FOUND = "요청 정보가 일치하지 않습니다.";
    public static final String NOT_USER_TURN = "유저의 차례가 아닙니다.";
    public static final String REDIS_ROCK_EXCEPTION = "다른 요청이 처리 중입니다.";
    public static final String VOTE_NOT_FOUND = "투표를 할 수 없습니다.";

}
