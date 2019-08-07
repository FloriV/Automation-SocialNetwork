package support;

import dtos.PostDTO;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PostService extends BaseService {

    public Response postContent(String payload) {
        return given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post(baseURL + "/posts");
    }

    public Response deletePost(int postId) {
        return given()
                .contentType(ContentType.JSON)
                .delete(baseURL + "/posts/" + postId);
    }

    public Response getPost_byId(String postId) {
        return given()
                .contentType(ContentType.JSON)
                .get(baseURL + "/posts/" + postId);
    }

    public Response getPosts() {
        return given()
                .contentType(ContentType.JSON)
                .get(baseURL + "/posts");
    }

    public PostDTO createPostTextPayload(String userId, String title, String body) {
        PostDTO post_dto = new PostDTO();
        post_dto.setUserId(userId);
        post_dto.setTitle(title);
        post_dto.setBody(body);
        return post_dto;
    }
}

