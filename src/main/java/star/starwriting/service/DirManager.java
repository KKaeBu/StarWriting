package star.starwriting.service;

import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DirManager {

    private final String rootPath = System.getProperty("user.dir");
    private final String membersDirPath = rootPath + PathSeperator("/src/main/resources/static/members/");
    /**
     * 회원가입시 맴버별 폴더 생성
     * */
    public void makeMemberDir(Long memberId) {
        String memberDirPath = membersDirPath + memberId.toString();
        File memberDir = new File(memberDirPath);

        if(memberDir.exists())
            return;

        try{
            memberDir.mkdir();
        }
        catch (Exception e) {
            e.getStackTrace();
        }
    }

    private String PathSeperator(String path) {
        String[] split = path.split("/");
        String joinPath = File.separator + String.join(File.separator, split) + File.separator;

        return joinPath;
    }
}
