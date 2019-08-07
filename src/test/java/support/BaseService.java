package support;

import io.restassured.response.Response;
import org.junit.Assert;


public class BaseService {

    String baseURL = "https://jsonplaceholder.typicode.com";

    public void verifyStatusCode(Response response, int expectedStatusCode) {
        Assert.assertEquals("Status code returned is not correct!", expectedStatusCode, response.statusCode());
    }

    public void verifyResponseEmpty(Response response) {
        Assert.assertEquals("Response json should be empty!", "{}", response.getBody().path("").toString());
    }
}
