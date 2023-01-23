package liar.game.business.game.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GameResultQueryRepositoryImpl implements GameResultQueryRepository {

    private final JPAQueryFactory query;

}
