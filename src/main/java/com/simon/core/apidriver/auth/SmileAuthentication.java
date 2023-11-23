package com.simon.core.apidriver.auth;

import com.simon.core.api.ApiResponse;
import com.simon.core.config.ConfigKeys;
import com.simon.core.config.Configurator;
import com.simon.core.reporter.ReportFactor;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Base64;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpHeaders.AUTHORIZATION;

public record SmileAuthentication(Configurator configurator) implements IAuthentication {
    private static final String DEFAULT_LOGIN_URL = "/api/v1/login";

    @Override
    public ApiResponse login(String username, String password) {
        String loginUrl = configurator.getBackendUrl() + configurator.getParameterOrDefault(ConfigKeys.LOGIN_URI, DEFAULT_LOGIN_URL);
        RequestSpecification request = given()
                .header(AUTHORIZATION, getBasicToken(username, password));
        ReportFactor.reportRequestUrl("POST", loginUrl);
        ReportFactor.reportRequestHeader(request, true);
        Response response = request.post(loginUrl);
        ReportFactor.reportResponseHeaders(response, true);
        ReportFactor.reportResponseBody(response, true);
        return ApiResponse.of(response);
    }

    private String getBasicToken(String username, String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }
}
