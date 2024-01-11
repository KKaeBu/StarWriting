package star.starwriting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import star.starwriting.domain.Member;
import star.starwriting.domain.Post;
import star.starwriting.domain.PostComment;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCommentRequestDto {

  private String mainText;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private String commentDate;
  private String member;
  private Long post;

  public PostComment toEntity(Member member, Post post) {
    return PostComment.builder()
        .mainText(this.mainText)
        .member(member)
        .post(post)
        .commentDate(this.commentDate)
        .build();
  }
}
