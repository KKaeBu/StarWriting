package star.starwriting.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "post_comment_tb")
@Entity
public class PostComment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "main_text", length = 100, nullable = false)
  private String mainText;

  @Column(name = "comment_date")
  private String commentDate;

  // following member
  @OneToOne
  @JoinColumn(name = "post_id")
  private Post post;

  // following member
  @OneToOne
  @JoinColumn(name = "member_id")
  private Member member;
}
