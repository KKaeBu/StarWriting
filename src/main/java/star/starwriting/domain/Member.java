package star.starwriting.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member_tb")
@Entity
public class Member implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "member_id", length = 20, nullable = false, unique = true)
  private String memberId;
  @Column(length = 200, nullable = false)
  private String password;
  @Column(length = 20, nullable = false)
  private String name;
  @Column()
  @Temporal(TemporalType.DATE)
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date birthday;
  @Column(nullable = false)
  private String email;
  @Column(name = "phone_num")
  private String phoneNum;
  @Column(length = 15)
  private String nickname;
  private String tier;
  private String address;
  @Column(nullable = false)
  private String sex;
  @Column(name = "create_date")
  private String createDate;
  // 이미지 파일
  @OneToOne
  @JoinColumn(name = "profile_image")
  private MemberProfileImage profileImage;

  // following member
  @JsonIgnore
  @OneToMany
  @JoinColumn(name = "following_member")
  private List<Member> followingMember;

  // followed member
  @JsonIgnore
  @OneToMany
  @JoinColumn(name = "followed_member")
  private List<Member> followedMember;

  // Post 좋아요
  @JsonIgnore
  @OneToMany
  @JoinColumn(name = "like_post", referencedColumnName = "id")
  private List<Post> likePost;

  // 데이터 기본값을 설정해주기 위한 어노테이션 및 메소드
  @PrePersist
  public void prePersist() {
    this.tier = this.tier == null ? "BRONZE" : this.tier;
  }
}
