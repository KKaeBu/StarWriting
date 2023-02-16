package star.starwriting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import star.starwriting.domain.Member;
import star.starwriting.domain.MemberProfileImage;

import javax.persistence.Column;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfileImageDto {
    private String originalFileName;
    private String storeFileName;
    private String fileUrl;

    public MemberProfileImage toEntity() {
        return MemberProfileImage.builder()
                .originalFileName(this.originalFileName)
                .storeFileName(this.storeFileName)
                .fileUrl(this.fileUrl)
                .build();
    }
}
