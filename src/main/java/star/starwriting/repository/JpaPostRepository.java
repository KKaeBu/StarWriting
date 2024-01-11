package star.starwriting.repository;

import org.springframework.stereotype.Repository;
import star.starwriting.domain.Post;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaPostRepository implements PostRepository {

  EntityManager em;

  public JpaPostRepository(EntityManager em) {
    this.em = em;
  }

  @Override
  public Post save(Post post) {
    em.persist(post);
    return post;
  }

  @Override
  public Optional<Post> findById(Long id) {
    Post post = em.find(Post.class, id);
    return Optional.ofNullable(post);
  }

  @Override
  public Optional<Post> findByTitle(String title) {
    Post post = em.find(Post.class, title);
    return Optional.ofNullable(post);
  }

  @Override
  public List<Post> findAll() {
    List<Post> result = em.createQuery("select p from Post p", Post.class)
        .getResultList();
    return result;
  }

  @Override
  public List<Post> findMemberAll(String memberId) {
    List<Post> result = em.createQuery("select p from Post p where p.member = :memberId",
            Post.class)
        .getResultList();
    return result;
  }
}
