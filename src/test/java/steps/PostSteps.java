package steps;

import com.google.gson.Gson;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import dtos.PostDTO;
import io.restassured.response.Response;
import org.junit.Assert;
import support.PostService;

public class PostSteps {
    private PostService post = new PostService();
    private Response response = null;
    private String postId;
    private String payload;
    private PostDTO post_dto;

    @Given("^I want to write a post with (.*), (.*) and (.*)$")
    public void set_postData(String userId, String postTitle, String postContent) {
        post_dto = post.createPostTextPayload(userId, postTitle, postContent);
        payload = new Gson().toJson(post_dto);
        System.out.println("Payload sent!:" + payload);
    }

    @When("^I make a request to post on the social network$")
    public void iMakeARequestToPostTextOnTheSocialNetwork() {
        response = post.postContent(payload);
        System.out.println("Post response " + response.prettyPrint());
        postId = response.getBody().path("id").toString();
        System.out.println("Post id returned " + postId);
    }

    @When("^I make a request to get post$")
    public void iMakeARequestToGetPost() {
        response = post.getPost_byId(postId);
        System.out.println("Get post by id response " + response.prettyPrint());
    }

    @When("^I make a request to get post with (.*)$")
    public void iRequestPostByGivenId(String id) {
        response = post.getPost_byId(id);
        System.out.println("Get post with " + id + " response " + response.prettyPrint());
        postId = id;
    }

    @When("^I make a request to get all posts$")
    public void iMakeARequestToGetAllPosts() {
        response = post.getPosts();
        System.out.println("Get all posts response " + response.prettyPrint());
    }

    @When("^I delete post by (.*)$")
    public void iDeleteOnePost(int postId) {
        response = post.deletePost(postId);
        System.out.println("Delete response: " + response.prettyPrint());
    }

    @Then("^post status code is (.*)$")
    public void verifyStatusCode(int statusCode) {
        post.verifyStatusCode(response, statusCode);
    }

    @Then("^correct post should be returned$")
    public void correctPostShouldBeReturned() {
        Assert.assertEquals("Post returned does not have the correct id!", postId, response.getBody().path("id").toString());
        Assert.assertTrue("Post returned does not have the correct userId!", (payload.contains(response.getBody().path("userId").toString())));
        Assert.assertTrue("Post returned does not have the correct title!", (payload.contains(response.getBody().path("title").toString())));
        Assert.assertTrue("Post returned does not have the correct body!", (payload.contains(response.getBody().path("body").toString())));
    }

    @Then("^post should not be found in list$")
    public void postShouldNotBeFoundInList() {
        String savedPosts = response.getBody().path("").toString();
        System.out.println("get all post response: /n" + savedPosts);
        Assert.assertFalse("No posts with the invalid userID should be returned", savedPosts.contains("\"userId\": " + post_dto.getUserId()));
        Assert.assertFalse("No posts with the invalid title should be returned", savedPosts.contains("\"userId\": " + post_dto.getTitle()));
        Assert.assertFalse("No posts with the invalid body should be returned", savedPosts.contains("\"userId\": " + post_dto.getBody()));
    }

    @Then("post returned should have valid data")
    public void postReturnedShouldHaveValidData() {
        Assert.assertEquals("Post returned does not have the correct id!", postId, response.getBody().path("id").toString());
        Assert.assertFalse("Post returned does not have a valid userId!", response.getBody().path("userId").toString().isEmpty());
        Assert.assertFalse("Post returned does not have a valid title!", response.getBody().path("title").toString().isEmpty());
        Assert.assertFalse("Post returned does not have a valid body!", response.getBody().path("body").toString().isEmpty());
    }

    @Then("post response should be empty")
    public void responseShouldBeEmpty() {
        post.verifyResponseEmpty(response);
    }
}
