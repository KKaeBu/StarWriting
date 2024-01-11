package star.starwriting.repository;

import star.starwriting.domain.MemberProfileImage;

import java.util.Optional;

public interface MemberProfileImageRepository {

  MemberProfileImage save(MemberProfileImage memberProfileImage); //프로필 이미지 정보 저장

  Optional<MemberProfileImage> findById(Long id); //id로 이미지 찾아서 반환
}
