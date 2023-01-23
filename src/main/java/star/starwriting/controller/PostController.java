package star.starwriting.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public Long savePost(@RequestBody PostRequestDto requestDto) {
        return postService.join(requestDto);
    }
}
