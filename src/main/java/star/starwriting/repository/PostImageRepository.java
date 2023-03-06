package star.starwriting.repository;

import star.starwriting.domain.MemberProfileImage;
import star.starwriting.domain.PostImage;

import java.util.Optional;

public interface PostImageRepository {
    PostImage save(PostImage postImage); //작성 글 이미지 정보 저장
    Optional<PostImage> findById(Long id); //id로 이미지 찾아서 반환
}
