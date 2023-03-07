package star.starwriting.repository;


import star.starwriting.domain.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Post save(Post post); //새로운 글 작성
    Optional<Post> findById(Long id); //id로 찾아서 반환
    Optional<Post> findByTitle(String title); //제목으로 찾아서 반환
    List<Post> findAll(); //모든 글 반환
    List<Post> findMemberAll(String memberId); //특정 회원이 작성한 모든 글 반환
}
