package star.starwriting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import star.starwriting.domain.Member;
import star.starwriting.domain.Post;
import star.starwriting.repository.JpaMemberRepository;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {

  private String title;
  private String member;
  private String mainText;
  private PostImageDto postImage;

  public Post toEntity(Member member) {
    return Post.builder()
        .title(this.title)
        .member(member)
        .mainText(this.mainText)
        .build();
  }
}
