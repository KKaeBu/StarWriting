package star.starwriting.domain;

import lombok.*;

import javax.persistence.*;
import java.io.File;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member_profile_img_tb")
@Entity
public class MemberProfileImage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "original_file_name")
  private String originalFileName;
  @Column(name = "store_file_name")
  private String storeFileName;
  @Column(name = "file_url")
  private String fileUrl;
  @Column(name = "full_path")
  private String fullPath;
}
