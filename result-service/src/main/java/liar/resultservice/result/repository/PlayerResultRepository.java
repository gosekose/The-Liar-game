package liar.resultservice.result.repository;

import liar.resultservice.result.domain.PlayerResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.concurrent.atomic.AtomicLong;

@Repository
public interface PlayerResultRepository extends JpaRepository<PlayerResult, AtomicLong> {
}