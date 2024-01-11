package star.starwriting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowRequestDto {

  private String followingMemberId; // 팔로우 하는 쪽의 id
  private String followedMemberId; // 팔로우 당하는 쪽의 id
}
