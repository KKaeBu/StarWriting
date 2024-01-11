package star.starwriting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import star.starwriting.domain.Member;
import star.starwriting.domain.MemberProfileImage;
import star.starwriting.domain.PostImage;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

  private Long id;
  private Date posting_date;
  private Long shared_num;
  private String title;
  private Long view;
  private Member member;
  private PostImage postImage;
}
