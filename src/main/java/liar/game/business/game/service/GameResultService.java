package liar.game.business.game.service;

import liar.game.business.game.repository.GameResultRepository;
import liar.game.business.game.repository.dto.AllMemberGameResultAnalysisInterface;
import liar.game.business.game.repository.dto.AllMemberGameResultAnalysisJpqlInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GameResultService {

    private final GameResultRepository repository;

    public Page<AllMemberGameResultAnalysisInterface> fetchAllMembersGameResultAnalysis(Pageable pageable) {
        return repository.fetchAllMembersGameResultAnalysis(pageable);
    }

    public Page<AllMemberGameResultAnalysisJpqlInterface> fetchAllMembersGameResultAnalysisJpql(Pageable pageable) {
        return repository.fetchAllMembersGameResultAnalysisJpql(pageable);
    }

}
