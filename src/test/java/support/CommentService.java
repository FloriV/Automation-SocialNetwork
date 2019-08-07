package support;

import dtos.CommentDTO;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CommentService extends BaseService {

    public Response postComment(String payload) {
        return given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post(baseURL + "/comments");
    }

    public Response getComment_byId(String commentId) {
        return given()
                .contentType(ContentType.JSON)
                .get(baseURL + "/comments/" + commentId);
    }

    public Response getPostComments_byId(String postID) {
        return given()
                .contentType(ContentType.JSON)
                .get(baseURL + "/comments?postId=" + postID);
    }

    public CommentDTO createCommentTextPayload(String postId, String name, String email, String body) {
        CommentDTO comment_dto = new CommentDTO();
        comment_dto.setPostId(postId);
        comment_dto.setName(name);
        comment_dto.setEmail(email);
        comment_dto.setBody(body);
        return comment_dto;
    }
}
