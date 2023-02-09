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

    @PrePersist
    public void prePersist(){
        String imgFileUrl = "src/main/resources/static/img/basicProfile.png"
        File basicImgFile = new File("src/main/resources/static/img/basicProfile.png");
        String storedImgFileName = UUID.randomUUID() + "." + extractExt(basicImgFile.getName());

        this.originalFileName = this.originalFileName == null ? basicImgFile.getName() : this.originalFileName;
        this.storeFileName = this.storeFileName == null ? storedImgFileName : this.storeFileName;
        this.fileUrl = this.fileUrl == null ? imgFileUrl : this.fileUrl;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
