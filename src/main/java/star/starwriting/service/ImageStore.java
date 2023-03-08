package star.starwriting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import star.starwriting.domain.Member;
import star.starwriting.domain.MemberProfileImage;
import star.starwriting.domain.Post;
import star.starwriting.domain.PostImage;
import star.starwriting.dto.MemberProfileImageDto;
import star.starwriting.dto.PostImageDto;
import star.starwriting.repository.MemberProfileImageRepository;
import star.starwriting.repository.PostImageRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;
import java.io.File;

import java.io.IOException;

@Component
public class ImageStore {
    private final MemberProfileImageRepository memberProfileImageRepository;
    private final PostImageRepository postImageRepository;
    @Autowired
    public ImageStore(MemberProfileImageRepository memberProfileImageRepository, PostImageRepository postImageRepository) {
        this.memberProfileImageRepository = memberProfileImageRepository;
        this.postImageRepository = postImageRepository;
    }

    // 루트 경로 불러오기
    private final String rootPath = System.getProperty("user.dir");
    // 프로젝트 루트 경로에 있는 files 디렉토리
    private final String membersPath = pathSeperator("src/main/resources/static/members");
    private final String membersDir = rootPath + pathSeperator("src/main/resources/static/members");
    private final String basicPostImgPath = rootPath + pathSeperator("src/main/resources/static/img/postImg");
    private final String basicProfileImgPath = rootPath + pathSeperator("src/main/resources/static/img/profileImg");

    public String getFullPath(String filename) {
        return "/img/" + filename;
    }

    public MemberProfileImage storeProfileImage(MultipartFile file, Member member) throws IOException {

        // 기본 프로필 이미지 저장
        // MultipartFile은 null이 아니라 isEmpty로 null체크
        // 파라미터로 받아온 file이 null일 경우 프로필이미지를 기본 이미지로 설정
        if(file.isEmpty()) {
            String originalFilename = "basicProfile.png";
            String storeFileName = "basicProfile.png";
            String fileUrl = basicProfileImgPath + storeFileName;

            MemberProfileImageDto profileImageDto = new MemberProfileImageDto(
                    originalFilename,
                    storeFileName,
                    fileUrl,
                    "C:\\Users\\82109\\Desktop\\웹\\StarWriting\\src\\main\\resources\\static\\img\\basicProfile.png"
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
        String storeFileName = nowTimeStamp() + "-" + UUID.randomUUID() + "." + extractExt(originalFilename);

//        String fileUrl = PathSeperator("src/main/resources/static/img/") +storeFileName;
//        String fullPath = fileDir + storeFileName;
//        System.out.println("파일경로: " + fileDir);

        String fileUrl = membersPath + pathSeperator(member.getMemberId() + "/profileImg") + storeFileName;
        String fullPath = membersDir + pathSeperator(member.getMemberId() + "/profileImg") + storeFileName;



        // 파일을 저장하는 부분 -> 파일경로 + storeFilename 에 저장
        file.transferTo(new File(fullPath));

        MemberProfileImageDto profileImageDto = new MemberProfileImageDto(
                originalFilename,
                storeFileName,
                fileUrl,
                fullPath
        );

        MemberProfileImage profileImage = profileImageDto.toEntity();
        memberProfileImageRepository.save(profileImage);
        member.setProfileImage(profileImage); // 회원가입한 멤버의 profileImage에 저장한 이미지를 할당

        return profileImage;
    }

    // 글 작성시 사용된 이미지 저장 (미완)
    public PostImage storePostImage(String memberId, Post post, MultipartFile file) throws IOException {
        File postImgDir = new File(basicPostImgPath);
        // 배경 이미지가 없다면 기본 이미지중 랜덤으로 설정
        if(file.isEmpty()) {

            int bgDefaultFileCount = dirFileCount(postImgDir);

            Random random = new Random();
            int bgImgNum = random.nextInt(bgDefaultFileCount) + 1;
            System.out.println("bgImgNum: " + bgImgNum);

            String originalFileName = chooseBgImg(postImgDir, bgImgNum);
            String storeFileName = originalFileName;
            String fileUrl = basicPostImgPath + pathSeperator(storeFileName);

            PostImageDto postImageDto = new PostImageDto(
                    originalFileName,
                    storeFileName,
                    fileUrl
            );

            PostImage postImage = postImageDto.toEntity();
            postImageRepository.save(postImage);
            post.setPostImage(postImage); // 작성 글의 postImage에 해당 이미지를 할당

            return null;
        }

        // file이 null이 아니라면 아래로
        String originalFileName = file.getOriginalFilename();
        String storeFileName = nowTimeStamp() + "-" + UUID.randomUUID() + "." + extractExt(originalFileName);

        String fileUrl = membersPath + pathSeperator(memberId + "/posts/" + post.getTitle()) + storeFileName;
        String fullPath = membersDir + pathSeperator(memberId + "/posts/" + post.getTitle()) + storeFileName;



        // 파일을 저장하는 부분 -> 파일경로 + storeFilename 에 저장
        file.transferTo(new File(fullPath));

        PostImageDto postImageDto = new PostImageDto(
                originalFileName,
                storeFileName,
                fileUrl
        );

        PostImage postImage = postImageDto.toEntity();
        postImageRepository.save(postImage);
        post.setPostImage(postImage); // 작성 글의 postImage에 해당 이미지를 할당

        return postImage;
    }

    // 확장자 추출
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    private String pathSeperator(String path) {
        String[] split = path.split("/");
        String joinPath = File.separator + String.join(File.separator, split) + File.separator;

        return joinPath;
    }

    private String nowTimeStamp() {
        // 현재 날짜 구하기
        LocalDateTime now = LocalDateTime.now();
        // 포맷 정의
        String formatDate = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        return formatDate;
    }

    private int dirFileCount(File dir) {
        int count = 0;
        try {
            for (File file : dir.listFiles()) {
                if (file.isFile())
                    count++;  //폴더 내부 파일 갯수
                else
                    count += dirFileCount(file);
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return count;
    }

    private String chooseBgImg(File dir, int bgNum) {
        String fileName = "";
        try {
            File[] files = dir.listFiles();
            for(int i = 0; i < files.length; i++){
                if(i == bgNum){
                    fileName = files[i].getName();
                    break;
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }

        return fileName;
    }

}
