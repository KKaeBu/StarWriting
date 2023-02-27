package star.starwriting.controller;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import star.starwriting.dto.PostCommentRequestDto;
import star.starwriting.dto.PostRequestDto;
import star.starwriting.service.PostService;

@RestController
public class PostController {
    private final PostService postService;
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/api/posts") /* POST 요청의 Header에서 키값이 Authorization인 값을 받는다 -> 토큰 받아온다  */
    public ResponseEntity<String> savePost(@RequestHeader(value = "Authorization")String token, @RequestBody PostRequestDto requestDto) {
        boolean httpState = postService.post(requestDto,token);
        if (httpState) {
            return new ResponseEntity<>("글이 성공적으로 작성되었습니다.", HttpStatus.CREATED); /* http state code 201 반환 */
        }else{
            return new ResponseEntity<>("글 작성에 실패했습니다.", HttpStatus.BAD_REQUEST); /* http state code 400 반환 */
        }
    }

    @PostMapping("/api/comments")
    public ResponseEntity<String> saveComment(@RequestHeader(value = "Authorization")String token, @RequestBody PostCommentRequestDto postCommentRequestDto){
        boolean httpState = postService.comment(postCommentRequestDto,token);
        if (httpState) {
            return new ResponseEntity<>("댓글이 성공적으로 작성되었습니다.", HttpStatus.CREATED); /* http state code 201 반환 */
        }else{
            return new ResponseEntity<>("댓글 작성에 실패했습니다.", HttpStatus.BAD_REQUEST); /* http state code 400 반환 */
        }
    }
}
