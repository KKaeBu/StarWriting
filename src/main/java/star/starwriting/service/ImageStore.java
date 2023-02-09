package star.starwriting.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import star.starwriting.domain.MemberProfileImage;
import java.util.UUID;
import java.io.File;

import java.io.IOException;

@Component
public class ImageStore {
    // 루트 경로 불러오기
    private final String rootPath = System.getProperty("user.dir");
    // 프로젝트 루트 경로에 있는 files 디렉토리
    private final String fileDir = rootPath + "/files/";

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public MemberProfileImage storeImage(MultipartFile multipartFile) throws IOException {

        if(multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        // 작성자가 업로드한 파일명 -> 서버 내부에서 관리하는 파일명
        // 파일명을 중복되지 않게끔 UUID로 정하고 ".확장자"는 그대로
        String storeFilename = UUID.randomUUID() + "." + extractExt(originalFilename);

        // 파일을 저장하는 부분 -> 파일경로 + storeFilename 에 저장
        multipartFile.transferTo(new File(getFullPath(storeFilename)));

        MemberProfileImage profileImage = new MemberProfileImage();
        profileImage.setOriginalFileName(originalFilename);
        profileImage.setStoreFileName(storeFilename);
        profileImage.setFileUrl(getFullPath(storeFilename));

        return profileImage;
    }

    // 확장자 추출
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

}
