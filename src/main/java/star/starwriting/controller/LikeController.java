package star.starwriting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import star.starwriting.dto.LikeRequestDto;
import star.starwriting.service.LikeService;

@RestController
public class LikeController {
    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/api/likes")
    public ResponseEntity likePost(@RequestHeader(value = "Authorization")String token, @RequestBody LikeRequestDto likeRequestDto){
        boolean httpState = likeService.like(likeRequestDto, token);
        if(httpState){
            return new ResponseEntity(HttpStatus.CREATED); /* http state code 201 반환 */
        }else{
            return new ResponseEntity(HttpStatus.BAD_REQUEST); /* http state code 400 반환 */
        }
    }
}
