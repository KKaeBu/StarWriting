package star.starwriting.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "post_img_tb")
@Entity
public class PostImage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "original_file_name")
  private String originalFileName;
  @Column(name = "store_file_name")
  private String storeFileName;
  @Column(name = "file_url")
  private String fileUrl;

}
