package star.starwriting.repository;

import star.starwriting.domain.PostComment;

import java.util.List;

public interface PostCommentRepository {

  PostComment save(PostComment postComment);

  List<PostComment> findAll();
}
