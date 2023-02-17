package star.starwriting.repository;

import star.starwriting.domain.Member;
import star.starwriting.domain.Post;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member); //회원 저장
    Member update(Member member); // 회원 업데이트
    Optional<Member> findById(Long id); //id로 찾아서 반환
    Optional<Member> findByName(String name); //이름으로 찾아서 반환
    Optional<Member> findByMemberId(String memberId); //멤버아이디로 찾아서 반환
    List<Member> findAll(); //모든 회원 반환
}
