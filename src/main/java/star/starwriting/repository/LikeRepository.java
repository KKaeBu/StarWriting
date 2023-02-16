package star.starwriting.repository;

import star.starwriting.domain.Like;

import java.util.List;
import java.util.Optional;

public interface LikeRepository {
    Like save(Like like);

    Optional<Like> findById(Long id);

    Optional<Like> findByMemberId(String memberId);

    Optional<Like> findByPostId(Long id);

    List<Like> findAll();
}
