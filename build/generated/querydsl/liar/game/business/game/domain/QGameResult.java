package liar.game.business.game.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGameResult is a Querydsl query type for GameResult
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGameResult extends EntityPathBase<GameResult> {

    private static final long serialVersionUID = -2036626769L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGameResult gameResult = new QGameResult("gameResult");

    public final liar.game.common.QBaseEntity _super = new liar.game.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QGame game;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final liar.game.member.domain.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final EnumPath<Result> result = createEnum("result", Result.class);

    public QGameResult(String variable) {
        this(GameResult.class, forVariable(variable), INITS);
    }

    public QGameResult(Path<? extends GameResult> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGameResult(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGameResult(PathMetadata metadata, PathInits inits) {
        this(GameResult.class, metadata, inits);
    }

    public QGameResult(Class<? extends GameResult> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.game = inits.isInitialized("game") ? new QGame(forProperty("game"), inits.get("game")) : null;
        this.member = inits.isInitialized("member") ? new liar.game.member.domain.QMember(forProperty("member")) : null;
    }

}

