package steps;

import com.google.gson.Gson;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import dtos.CommentDTO;
import io.restassured.response.Response;
import org.junit.Assert;
import support.CommentService;

public class CommentSteps {
    private CommentService comment = new CommentService();
    private Response response;
    private String payload;
    private String commentId;
    private CommentDTO comment_dto;

    @Given("^I want to write a comment with (.*), (.*), (.*) and (.*)$")
    public void set_commentData(String postId, String name, String email, String body) {
        comment_dto = comment.createCommentTextPayload(postId, name, email, body);
        payload = new Gson().toJson(comment_dto);
        System.out.println("Payload sent!:" + payload);
    }

    @When("I make a request to post comment on the social network")
    public void iMakeARequestToPostCommentOnTheSocialNetwork() {
        response = comment.postComment(payload);
        System.out.println("Response to post comment!:" + response.prettyPrint());
        String commentId = response.getBody().path("id").toString();
        System.out.println("New comment id is: " + commentId);
    }

    @When("I make a request to get post comments by postId")
    public void iMakeARequestToGetPostCommentsByPostId() {
        response = comment.getPostComments_byId(comment_dto.getPostId());
    }

    @When("I make a request to get comment with (.*)")
    public void iMakeARequestToGetCommentWithId(String id) {
        response = comment.getComment_byId(id);
        commentId = id;
    }

    @Then("^comment status code is (.*)$")
    public void verifyStatusCode(int statusCode) {
        comment.verifyStatusCode(response, statusCode);
    }

    @Then("new comment should be added")
    public void newCommentShouldBeAdded() {
        String commentsList = response.getBody().path("").toString();
        System.out.println("get all post response: /n" + commentsList);
        Assert.assertTrue("CommentId was not found in the comments list", commentsList.contains("\"id\": " + comment_dto.getId()));
        Assert.assertTrue("Email was not found in the comments list", commentsList.contains("\"email\": " + comment_dto.getEmail()));
        Assert.assertTrue("Name was not found in the comments list", commentsList.contains("\"name\": " + comment_dto.getName()));
        Assert.assertTrue("Body was not found in the comments list", commentsList.contains("\"body\": " + comment_dto.getBody()));
    }

    @Then("new comment should not be added")
    public void newCommentShouldNotBeAdded() {
        String commentsList = response.getBody().path("").toString();
        System.out.println("get all post response: /n" + commentsList);
        Assert.assertFalse("CommentId was not found in the comments list", commentsList.contains("\"id\": " + comment_dto.getId()));
        Assert.assertFalse("Email was not found in the comments list", commentsList.contains("\"email\": " + comment_dto.getEmail()));
        Assert.assertFalse("Name was not found in the comments list", commentsList.contains("\"name\": " + comment_dto.getName()));
        Assert.assertFalse("Body was not found in the comments list", commentsList.contains("\"body\": " + comment_dto.getBody()));
    }

    @Then("comment returned should have valid data")
    public void commentReturnedShouldHaveValidData() {
        Assert.assertEquals("Post returned does not have the correct id!", commentId, response.getBody().path("id").toString());
        Assert.assertFalse("Comment returned does not have a valid postId!", response.getBody().path("postId").toString().isEmpty());
        Assert.assertFalse("Comment returned does not have a valid name!", response.getBody().path("name").toString().isEmpty());
        Assert.assertFalse("Comment returned does not have a valid email!", response.getBody().path("email").toString().isEmpty());
        Assert.assertFalse("Comment returned does not have a valid body!", response.getBody().path("body").toString().isEmpty());
    }

    @Then("comment response should be empty")
    public void commentResponseShouldBeEmpty() {
        comment.verifyResponseEmpty(response);
    }
}
