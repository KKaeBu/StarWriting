package star.starwriting.repository;

import org.springframework.stereotype.Repository;
import star.starwriting.domain.Like;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaLikeRepository implements LikeRepository{
    EntityManager em;

    public JpaLikeRepository(EntityManager em){
        this.em = em;
    }

    @Override
    public Like save(Like like) {
        em.persist(like);
        return like;
    }

    @Override
    public Optional<Like> findById(Long id){
        Like like = em.find(Like.class, id);
        return Optional.ofNullable(like);
    }

    @Override
    public Optional<Like> findByMemberId(String memberId){
        Like like = em.find(Like.class, memberId);
        return Optional.ofNullable(like);
    }

    @Override
    public Optional<Like> findByPostId(Long id){
        Like like = em.find(Like.class, id);
        return Optional.ofNullable(like);
    }

    @Override
    public List<Like> findAll(){
        List<Like> result = em.createQuery("select l from Like l", Like.class)
                .getResultList();
        return result;
    }
}
