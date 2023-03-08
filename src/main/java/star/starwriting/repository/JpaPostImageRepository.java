package star.starwriting.repository;

import org.springframework.stereotype.Repository;
import star.starwriting.domain.MemberProfileImage;
import star.starwriting.domain.PostImage;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class JpaPostImageRepository implements PostImageRepository {
    private final EntityManager em;

    public JpaPostImageRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public PostImage save(PostImage postImage) {
        em.persist(postImage);
        return postImage;
    }

    @Override
    public Optional<PostImage> findById(Long id) {
        PostImage postImage = em.find(PostImage.class, id);
        return Optional.ofNullable(postImage);
    }
}
