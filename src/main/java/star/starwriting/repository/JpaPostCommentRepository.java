package star.starwriting.repository;

import org.springframework.stereotype.Repository;
import star.starwriting.domain.Post;
import star.starwriting.domain.PostComment;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class JpaPostCommentRepository implements PostCommentRepository{
    EntityManager em;

    public JpaPostCommentRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public PostComment save(PostComment postComment){
        em.persist(postComment);
        return postComment;
    }

    @Override
    public List<PostComment> findAll(){
        List<PostComment> result = em.createQuery("select p from PostComment p",PostComment.class)
                .getResultList();
        return result;
    }
}
