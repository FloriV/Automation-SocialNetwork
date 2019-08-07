package steps;

import com.google.gson.Gson;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import dtos.UserDTO;
import io.restassured.response.Response;
import org.junit.Assert;
import support.UserService;

import java.io.IOException;

public class UserSteps {
    private UserService user = new UserService();
    private Response response = null;
    private String userId;
    private String userData;
    private UserDTO user_dto;

    @Given("^I have valid user data$")
    public void set_userData() throws IOException {
        user_dto = user.readUserData();
        userData = new Gson().toJson(user_dto);
        System.out.println("User data to be sent!:" + userData);
    }

    @When("I make a request to create user on the social network")
    public void iMakeARequestToCreateUserOnTheSocialNetwork() {
        response = user.createUser(userData);
        userId = response.getBody().path("id").toString();
        System.out.println("Response to user creation!:" + response.prettyPrint());
    }

    @Then("user status code is (.*)")
    public void userStatusCodeIs(int statusCode) {
        user.verifyStatusCode(response, statusCode);
    }

    @When("I make a request to get users")
    public void iMakeARequestToGetUsers() {
        response = user.getUsers();
        System.out.println("Existing user list:" + response.prettyPrint());
    }

    @When("I make a request to get created user")
    public void iMakeARequestToGetUser() {
        response = user.getUser_byId(userId);
        System.out.println("User data retrieved:" + response.prettyPrint());
    }

    @When("I make a request to get user with (.*)")
    public void iMakeARequestToGetUserWithId(String id) {
        response = user.getUser_byId(id);
        System.out.println("User data retrieved:" + response.prettyPrint());
        userId = id;
    }

    @Then("new user should be added")
    public void newUserShouldBeAdded() {
        String usersList = response.getBody().path("").toString();
        System.out.println("get all post response: /n" + usersList);
        Assert.assertTrue("UserId was not found in the users list", usersList.contains("\"id\": " + user_dto.getId()));
        Assert.assertTrue("User email was not found in the users list", usersList.contains("\"email\": " + user_dto.getEmail()));
        Assert.assertTrue("User name was not found in the users list", usersList.contains("\"name\": " + user_dto.getName()));
        Assert.assertTrue("User username was not found in the users list", usersList.contains("\"userName\": " + user_dto.getUsername()));
        Assert.assertTrue("User username was not found in the users list", usersList.contains("\"phone\": " + user_dto.getPhone()));
    }

    @Then("correct user should be returned")
    public void correctUserShouldBeReturned() {
        Assert.assertEquals("User returned does not have the correct id!", userId, response.getBody().path("id"));
        Assert.assertTrue("User returned does not have the correct name!", (userData.contains(response.getBody().path("name").toString())));
        Assert.assertTrue("User returned does not have the correct userName!", (userData.contains(response.getBody().path("username").toString())));
        Assert.assertTrue("User returned does not have the correct email!", (userData.contains(response.getBody().path("email").toString())));
        Assert.assertTrue("User returned does not have the correct phone!", (userData.contains(response.getBody().path("phone").toString())));
    }

    @Then("user response should be empty")
    public void userResponseShouldBeEmpty() {
        user.verifyResponseEmpty(response);
    }

    @Then("user returned should have valid data")
    public void commentReturnedShouldHaveValidData() {
        Assert.assertEquals("User returned does not have the correct id!", userId, response.getBody().path("id").toString());
        Assert.assertFalse("User returned does not have a valid name!", response.getBody().path("name").toString().isEmpty());
        Assert.assertFalse("User returned does not have a valid username!", response.getBody().path("username").toString().isEmpty());
        Assert.assertFalse("User returned does not have a valid email!", response.getBody().path("email").toString().isEmpty());
        Assert.assertFalse("User returned does not have a valid phone!", response.getBody().path("phone").toString().isEmpty());
    }

}
