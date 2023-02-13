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
    private final String fileDir = rootPath + "/files/";

    public String getFullPath(String filename) {
        return "/img/" + filename;
    }

    public MemberProfileImage storeImage(MultipartFile file, Member member) throws IOException {

        // 프로필 이미지 저장
        if(file.isEmpty()) {
            return null;
        }

        String originalFilename = file.getOriginalFilename();
        // 작성자가 업로드한 파일명 -> 서버 내부에서 관리하는 파일명
        // 파일명을 중복되지 않게끔 UUID로 정하고 ".확장자"는 그대로
        String storeFileName = UUID.randomUUID() + "." + extractExt(originalFilename);
//        String storeFileName = file.getOriginalFilename();
        String fileUrl = "src/main/resources/static/img/"+storeFileName;
        String fullPath = "C:\\Users\\82109\\Desktop\\웹\\StarWriting\\src\\main\\resources\\static\\img\\" + storeFileName;

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
        member.setProfileImage(profileImage); // 이게 될지는 의문. member 객체를 파라미터로 넘겨, 다른 클래스의 함수에서 set 함수가 정상적으로 작동되는지 모르겠음

        return profileImage;
    }

    // 확장자 추출
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

}
