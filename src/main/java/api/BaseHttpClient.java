package api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BaseHttpClient {

    private RequestSpecification baseRequestSpec = new RequestSpecBuilder()
            .setBaseUri(URL.HOST)
            .addHeader("Content-Type", "application/json")
            .setRelaxedHTTPSValidation()
            .addFilter(new RequestLoggingFilter())
            .addFilter(new ResponseLoggingFilter())
            .addFilter(new ErrorLoggingFilter())
            .build();

    protected ValidatableResponse doPostRequest(String path, Object body) {
        return given()
                .spec(baseRequestSpec)
                .body(body)
                .post(path)
                .then();
    }

    protected ValidatableResponse doPostRequest(String path, Object body, String accessToken) {
        return given()
                .auth().oauth2(accessToken)
                .spec(baseRequestSpec)
                .body(body)
                .post(path)
                .then();
    }

    protected ValidatableResponse doDeleteRequest(String path, Object body, String accessToken) {
        return given()
                .spec(baseRequestSpec)
                .header("Authorization", accessToken)
                .delete(path)
                .then();
    }

    protected ValidatableResponse doPatchRequest(String path, Object body, String accessToken) {
        return given()
                .spec(baseRequestSpec)
                .header("Authorization", accessToken)
                .body(body)
                .patch(path)
                .then();
    }

    protected ValidatableResponse doGetRequest(String path) {
        return given()
                .spec(baseRequestSpec)
                .get(path)
                .then();
    }

    protected ValidatableResponse doGetRequest(String path, String accessToken) {
        return given()
                .spec(baseRequestSpec)
                .header("Authorization", accessToken)
                .get(path)
                .then();
    }
}