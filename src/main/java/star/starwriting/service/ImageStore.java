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

        // 멤버별 폴더 생성 -> 해당 멤버명 폴더가 없다면 생성한다.
        String folderPath = "C:\\Users\\82109\\Desktop\\웹\\StarWriting\\src\\main\\resources\\static\\img\\"+member.getMemberId();
        File folder = new File(folderPath);

        if(!folder.exists()){
            try {
                folder.mkdir();
                System.out.println(member.getMemberId() + "의 폴더가 생성되었습니다.");
            } catch (Exception e) {
                e.getStackTrace();
            }
        }else{
            System.out.println(member.getMemberId()+"의 폴더가 이미 존재합니다.");
        }

        // 프로필 이미지 저장

        // MultipartFile은 null이 아니라 isEmpty로 null체크
        // 파라미터로 받아온 file이 null일 경우 프로필이미지를 기본 이미지로 설정
        if(file.isEmpty()) {
            String originalFilename = "basicProfile.png";
            String storeFileName = "basicProfile.png";
            String fileUrl = "src/main/resources/static/img/"+storeFileName;

            MemberProfileImageDto profileImageDto = new MemberProfileImageDto(
                    originalFilename,
                    storeFileName,
                    fileUrl
            );

            MemberProfileImage profileImage = profileImageDto.toEntity();
            memberProfileImageRepository.save(profileImage);
            member.setProfileImage(profileImage); // 회원가입한 멤버의 profileImage에 저장한 이미지를 할당

            return null;
        }

        // file이 null이 아니라면 아래로
        String originalFilename = file.getOriginalFilename();
        // 작성자가 업로드한 파일명 -> 서버 내부에서 관리하는 파일명
        // 파일명을 중복되지 않게끔 UUID로 정하고 ".확장자"는 그대로
        String storeFileName = UUID.randomUUID() + "." + extractExt(originalFilename);
//      String storeFileName = file.getOriginalFilename();
        String fileUrl = "src/main/resources/static/img/"+member.getMemberId()+"/"+storeFileName;
        String fullPath = "C:\\Users\\82109\\Desktop\\웹\\StarWriting\\src\\main\\resources\\static\\img\\"+member.getMemberId()+"\\" + storeFileName;


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

}
