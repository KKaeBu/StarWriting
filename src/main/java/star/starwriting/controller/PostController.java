package star.starwriting.controller;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import star.starwriting.dto.PostRequestDto;
import star.starwriting.service.PostService;

@RestController
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/api/posts")
    public ResponseEntity<String> savePost(@RequestHeader(value = "Authorization")String token, @RequestBody PostRequestDto requestDto) {
        String httpState = postService.post(requestDto,token);
        return new ResponseEntity<>(httpState,HttpStatus.ACCEPTED);
    }
}
