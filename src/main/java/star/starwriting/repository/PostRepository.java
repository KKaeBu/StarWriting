package star.starwriting.repository;


import star.starwriting.domain.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Post save(Post post); //회원 저장
    Optional<Post> findById(Long id); //id로 찾아서 반환
    Optional<Post> findByTitle(String title); //제목으로 찾아서 반환
    List<Post> findAll(); //모든 회원 반환
}
