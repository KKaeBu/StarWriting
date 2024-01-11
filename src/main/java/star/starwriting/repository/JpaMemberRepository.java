package star.starwriting.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import star.starwriting.domain.Member;
import star.starwriting.domain.Post;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaMemberRepository implements MemberRepository {

  private final EntityManager em;

  public JpaMemberRepository(EntityManager em) {
    this.em = em;
  }

  @Override
  public Member save(Member member) {
    em.persist(member);
    return member;
  }

  @Override
  public Member update(Member member) {
    em.merge(member);
    return member;
  }

  @Override
  public Optional<Member> findById(Long id) {
    Member member = em.find(Member.class, id);
    return Optional.ofNullable(member);
  }

  @Override
  public Optional<Member> findByName(String name) {
    List<Member> result = em.createQuery("select m from Member m where m.name = :name",
            Member.class)
        .setParameter("name", name)
        .getResultList();
    return result.stream().findAny();
  }

  @Override
  public Optional<Member> findByMemberId(String memberId) {
    Member member = em.createQuery("select m from Member m where m.memberId = :memberId",
            Member.class)
        .setParameter("memberId", memberId)
        .getSingleResult();

    return Optional.ofNullable(member);
  }

  @Override
  public List<Member> findAll() {
    List<Member> result = em.createQuery("select m from Member m", Member.class)
        .getResultList();
    return result;
  }
}
