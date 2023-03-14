package star.starwriting.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import star.starwriting.dto.PostCommentRequestDto;
import star.starwriting.dto.PostRequestDto;
import star.starwriting.dto.PostResponseDto;
import star.starwriting.service.ImageStore;
import star.starwriting.service.PostService;

import java.io.IOException;
import java.io.InputStream;

@RestController
public class PostController {
    private final PostService postService;
    private final ImageStore imageStore;
    public PostController(PostService postService, ImageStore imageStore) {
        this.postService = postService;
        this.imageStore = imageStore;
    }

    // 글 작성
    @PostMapping("/api/posts") /* POST 요청의 Header에서 키값이 Authorization인 값을 받는다 -> 토큰 받아온다  */
    public ResponseEntity<String> savePost(@RequestHeader(value = "Authorization")String token, @RequestBody PostRequestDto requestDto, @RequestParam MultipartFile file) throws IOException {
        boolean httpState = postService.post(requestDto, token, file);
        if (httpState) {
            return new ResponseEntity<>("글이 성공적으로 작성되었습니다.", HttpStatus.CREATED); /* http state code 201 반환 */
        }else{
            return new ResponseEntity<>("글 작성에 실패했습니다.", HttpStatus.BAD_REQUEST); /* http state code 400 반환 */
        }
    }

    // 댓글 작성
    @PostMapping("/api/comments")
    public ResponseEntity<String> saveComment(@RequestHeader(value = "Authorization")String token, @RequestBody PostCommentRequestDto postCommentRequestDto){
        boolean httpState = postService.comment(postCommentRequestDto,token);
        if (httpState) {
            return new ResponseEntity<>("댓글이 성공적으로 작성되었습니다.", HttpStatus.CREATED); /* http state code 201 반환 */
        }else{
            return new ResponseEntity<>("댓글 작성에 실패했습니다.", HttpStatus.BAD_REQUEST); /* http state code 400 반환 */
        }
    }

    // 이미지 파일 전달
    @GetMapping(value = "/api/posts/{id}",produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] GetProfileImage(@PathVariable Long id) throws IOException {
        PostResponseDto post = postService.findPost(id).get();
        String fileName = post.getPostImage().getStoreFileName();
        String imgRootPath = "static/img/postImg";

        if(!imageStore.checkBgImg(fileName))
            imgRootPath = "static/members/" + post.getMemberId() + "/posts/" + post.getTitle() + "/img";

        String filePath = imageStore.pathSeperator(imgRootPath) + fileName;
        System.out.println("filePath: " + filePath);

        // getResourceAsStream()의 기본 path가 resources부터 시작임
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(filePath);
        System.out.println(in);
        return IOUtils.toByteArray(in);
    }
}
