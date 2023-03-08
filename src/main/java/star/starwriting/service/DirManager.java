package star.starwriting.service;

import org.springframework.stereotype.Component;
import star.starwriting.domain.Post;

import java.io.File;

@Component
public class DirManager {

    private final String rootPath = System.getProperty("user.dir");
    private final String membersDirPath = rootPath + pathSeperator("/src/main/resources/static/members/");

    /**
     * 회원가입시 맴버별 폴더 생성
     * */
    public void createMemberDir(String memberId) {
        String memberDirPath = membersDirPath + memberId; // 회원 개인 폴더
        String memberProfileImgPath = memberDirPath + pathSeperator("profileImg"); // 회원 폴더내 프로필 이미지 폴더
        String memberPostPath = memberDirPath + pathSeperator("posts"); // 회원 폴더내 포스팅 폴더

        File memberDir = new File(memberDirPath);
        File memberProfileImgDir = new File(memberProfileImgPath);
        File memberPostDir = new File(memberPostPath);

        // 이미 존재할시 탈출문
        if(memberDir.exists())
            return;

        try{
            memberDir.mkdir();
            memberProfileImgDir.mkdir();
            memberPostDir.mkdir();
        }
        catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void createPostDir(String memberId, Post post) {
        final String postsPath = membersDirPath + pathSeperator(memberId + "/posts");
        String postPath = postsPath + pathSeperator(post.getTitle());
        String postImgPath = postPath + pathSeperator("img");

        File postDir = new File(postPath);
        File postImgDir = new File(postImgPath);

        // 이미 작성된 글이라면 탈출 (중복글 방지)
        if(postDir.exists()) {
            System.out.println("이미 작성된 글입니다. 제목을 변경해주세요.");
            return;
        }

        try{
            postDir.mkdir();
            postImgDir.mkdir();
        }
        catch (Exception e){
            e.getStackTrace();
        }

    }

    private String pathSeperator(String path) {
        String[] split = path.split("/");
        String joinPath = File.separator + String.join(File.separator, split) + File.separator;

        return joinPath;
    }
}
