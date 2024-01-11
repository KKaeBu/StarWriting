package star.starwriting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import star.starwriting.domain.MemberProfileImage;
import star.starwriting.domain.PostImage;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostImageDto {

  private String originalFileName;
  private String storeFileName;
  private String fileUrl;

  public PostImage toEntity() {
    return PostImage.builder()
        .originalFileName(this.originalFileName)
        .storeFileName(this.storeFileName)
        .fileUrl(this.fileUrl)
        .build();
  }
}
