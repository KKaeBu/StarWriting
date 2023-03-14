package star.starwriting.controller;

import org.apache.commons.io.IOUtils;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpHeaders;
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
import star.starwriting.service.PostService;
import java.io.IOException;
import java.util.List;

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
    @ResponseBody
    public ResponseEntity<String> savePost(PostRequestDto requestDto, @RequestParam MultipartFile file) throws IOException {
        boolean httpState = postService.post(requestDto, file);
        if (httpState) {
            return new ResponseEntity<>("글이 성공적으로 작성되었습니다.", HttpStatus.CREATED); /* http state code 201 반환 */
        }else{
            return new ResponseEntity<>("글 작성에 실패했습니다.", HttpStatus.BAD_REQUEST); /* http state code 400 반환 */
        }
    }
    @CrossOrigin (origins = "http://localhost:3000" , exposedHeaders = "*")
    @PostMapping("/api/authorization") /* 토큰 검증 api */
    @ResponseBody
    public ResponseEntity<String> Auth(@RequestHeader(value = "Authorization", required=false) String token){
        System.out.println(token);
        boolean isValid = postService.isValidToken(token);
        if(isValid){
            return new ResponseEntity<>("토큰이 유효합니다", HttpStatus.OK); /* http state code 200 반환 */
        }else{
            return new ResponseEntity<>("토큰이 유효하지 않습니다", HttpStatus.BAD_REQUEST); /* http state code 400 반환 */
        }
    }
    @GetMapping(value = {"/api/posts"})
    @ResponseBody
    public List<PostResponseDto> getAllPosts(){
        return postService.findAllPosts();
    }

    @GetMapping(value = {"/api/posts/{id}"})
    @ResponseBody
    public PostResponseDto getPost(@PathVariable("id")Long id){
        PostResponseDto post = postService.findPost(id).get();
        return post;
    }

    // 댓글 작성
    @PostMapping("/api/comments")
    public ResponseEntity<String> saveComment(PostCommentRequestDto postCommentRequestDto){
        boolean isPost = postService.comment(postCommentRequestDto);
        if (isPost) {
            return new ResponseEntity<>("댓글이 성공적으로 작성되었습니다.", HttpStatus.CREATED); /* http state code 201 반환 */
        }else{
            return new ResponseEntity<>("댓글 작성에 실패했습니다.", HttpStatus.BAD_REQUEST); /* http state code 400 반환 */
        }
    }

    // 이미지 파일 전달
    @GetMapping(value = "/api/posts/{id}/image",produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] GetProfileImage(@PathVariable Long id) throws IOException {
        PostResponseDto post = postService.findPost(id).get();
        String fileName = post.getPostImage().getStoreFileName();
        String imgRootPath = "static/img/postImg";

        System.out.println("memberId: " + post.getMember().getMemberId());
        if(!imageStore.checkBgImg(fileName))
            imgRootPath = "static/members/" + post.getMember().getMemberId() + "/posts/" + post.getTitle() + "/img";

        String filePath = imageStore.pathSeperator(imgRootPath) + fileName;
        System.out.println("filePath: " + filePath);

        // getResourceAsStream()의 기본 path가 resources부터 시작임
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream in = loader.getResourceAsStream(filePath);
//        InputStream in = this.getClass().getClassLoader().getResourceAsStream(filePath);
        System.out.println(in);
        return IOUtils.toByteArray(in);
    }
}
