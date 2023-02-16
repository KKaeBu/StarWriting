package star.starwriting.repository;

import org.springframework.stereotype.Repository;
import star.starwriting.domain.Member;
import star.starwriting.domain.MemberProfileImage;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class JpaMemberProfileImageRepository implements MemberProfileImageRepository {
    private final EntityManager em;

    public JpaMemberProfileImageRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public MemberProfileImage save(MemberProfileImage memberProfileImage) {
        em.persist(memberProfileImage);
        return memberProfileImage;
    }

    @Override
    public Optional<MemberProfileImage> findById(Long id) {
        MemberProfileImage memberProfileImage = em.find(MemberProfileImage.class, id);
        return Optional.ofNullable(memberProfileImage);
    }
}
