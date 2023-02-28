package star.starwriting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import star.starwriting.domain.Member;
import star.starwriting.domain.MemberProfileImage;
import star.starwriting.dto.MemberProfileImageDto;
import star.starwriting.repository.MemberProfileImageRepository;

import java.util.UUID;
import java.io.File;

import java.io.IOException;

@Component
public class ImageStore {
    private final MemberProfileImageRepository memberProfileImageRepository;
    @Autowired
    public ImageStore(MemberProfileImageRepository memberProfileImageRepository) {
        this.memberProfileImageRepository = memberProfileImageRepository;
    }

    // 루트 경로 불러오기
    private final String rootPath = System.getProperty("user.dir");
    // 프로젝트 루트 경로에 있는 files 디렉토리
    private final String fileDir = rootPath + PathSeperator("/src/main/resources/static/img/");

    public String getFullPath(String filename) {
        return "/img/" + filename;
    }

    public MemberProfileImage storeImage(MultipartFile file, Member member) throws IOException {

        // 프로필 이미지 저장
        if(file == null) {
            return null;
        }

        String originalFilename = file.getOriginalFilename();
        // 작성자가 업로드한 파일명 -> 서버 내부에서 관리하는 파일명
        // 파일명을 중복되지 않게끔 UUID로 정하고 ".확장자"는 그대로
        String storeFileName = UUID.randomUUID() + "." + extractExt(originalFilename);
//        String storeFileName = file.getOriginalFilename();
        String fileUrl = PathSeperator("src/main/resources/static/img/") +storeFileName;
        String fullPath = fileDir + storeFileName;

        System.out.println("파일경로: " + fileDir);

        // 파일을 저장하는 부분 -> 파일경로 + storeFilename 에 저장
        file.transferTo(new File(fullPath));

        System.out.println(originalFilename);

        MemberProfileImageDto profileImageDto = new MemberProfileImageDto(
                originalFilename,
                storeFileName,
                fileUrl
        );

        MemberProfileImage profileImage = profileImageDto.toEntity();
        memberProfileImageRepository.save(profileImage);
        member.setProfileImage(profileImage); // 회원가입한 멤버의 profileImage에 저장한 이미지를 할당

        return profileImage;
    }

    // 확장자 추출
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    private String PathSeperator(String path) {
        String[] split = path.split("/");
        String joinPath = File.separator + String.join(File.separator, split) + File.separator;

        return joinPath;
    }

}
