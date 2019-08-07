package support;

import dtos.AddressDTO;
import dtos.CompanyDTO;
import dtos.GeoDTO;
import dtos.UserDTO;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class UserService extends BaseService {
    public Response createUser(String payload) {
        return given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post(baseURL + "/users");
    }

    public Response getUsers() {
        return given()
                .contentType(ContentType.JSON)
                .get(baseURL + "/users");
    }

    public Response getUser_byId(String userId) {
        return given()
                .contentType(ContentType.JSON)
                .get(baseURL + "/users/" + userId);
    }

    public UserDTO readUserData() throws IOException {
        UserDTO userData = new UserDTO();

        // Read data from file and map it to user dto
        InputStream input = new FileInputStream("src/test/resources/testingData/userData.properties");
        Properties prop = new Properties();
        prop.load(input);
        userData.setUsername(prop.getProperty("username"));
        userData.setEmail(prop.getProperty("email"));
        AddressDTO userAddress = new AddressDTO();
        userAddress.setStreet(prop.getProperty("address.street"));
        userAddress.setSuite(prop.getProperty("address.suite"));
        userAddress.setCity(prop.getProperty("address.city"));
        userAddress.setZipCode(prop.getProperty("address.zipcode"));
        GeoDTO userGeo = new GeoDTO();
        userGeo.setLat(prop.getProperty("address.geo.lat"));
        userGeo.setLng(prop.getProperty("address.geo.lng"));
        userAddress.setGeo(userGeo);
        userData.setAddress(userAddress);
        userData.setPhone(prop.getProperty("phone"));
        userData.setWebsite(prop.getProperty("website"));
        CompanyDTO userCompany = new CompanyDTO();
        userCompany.setName(prop.getProperty("company.name"));
        userCompany.setCatchPhrase(prop.getProperty("company.catchPhrase"));
        userCompany.setBs(prop.getProperty("company.bs"));
        userData.setCompany(userCompany);
        return userData;
    }
}
