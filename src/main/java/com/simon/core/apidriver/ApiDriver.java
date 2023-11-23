package com.simon.core.apidriver;

import com.simon.core.api.ApiResponse;
import com.simon.core.apidriver.auth.IAuthentication;
import com.simon.core.apidriver.auth.ILogin;
import com.simon.core.config.ConfigKeys;
import com.simon.core.reporter.ReportFactor;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.apache.http.HttpHeaders.AUTHORIZATION;

@Slf4j
public class ApiDriver implements RestAPI {
    private static final String DEFAULT_TIMEOUT = "60";
    private static final String SOCKET_TIMEOUT = "http.socket.reuseaddr";
    private static final String CONNECTION_TIMEOUT = "http.connection.timeout";
    private static final String TOKEN_PATH = "data.token";
    private String bearerToken;
    private final RequestSpecification request;
    private final IAuthentication auth;
    public final Map<String, String> paramMap = new HashMap<>();
    public final Map<String, String> queryMap = new ConcurrentHashMap<>();
    public final Map<String, String> pathMap = new ConcurrentHashMap<>();
    private final int timeout;
    private final boolean isDeepReporting;
    private final String backendUrl;

    public ApiDriver(IAuthentication auth) {
        this.auth = auth;
        request = given().contentType(ContentType.JSON).accept(ContentType.JSON);
        backendUrl = auth.configurator().getBackendUrl();
        timeout = Integer.parseInt(auth.configurator().getParameterOrDefault(ConfigKeys.SOCKET_TIMEOUT, DEFAULT_TIMEOUT));
        isDeepReporting = Boolean.parseBoolean(auth.configurator().getParameterOrDefault(ConfigKeys.DEEP_REPORTING, "false"));
    }

    @Override
    public ApiResponse get(String url) {
        return get(url, null);
    }

    @Override
    public ApiResponse get(String url, String payload) {
        url = backendUrl + url;
        RequestSpecification request = generateRequestSpecification(payload);
        ReportFactor.reportRequestUrl("GET", url);
        ReportFactor.reportRequestHeader(request, isDeepReporting);
        Response response = request.get(url);
        ReportFactor.reportResponseHeaders(response, isDeepReporting);
        ReportFactor.reportResponseBody(response, isDeepReporting);
        return ApiResponse.of(response);
    }

    @Override
    public ApiResponse post(String url, String payload) {
        url = backendUrl + url;
        RequestSpecification request = generateRequestSpecification(payload);
        ReportFactor.reportRequestUrl("POST", url);
        ReportFactor.reportRequestHeader(request, isDeepReporting);
        ReportFactor.reportRequestBody(payload, isDeepReporting);
        Response response = request.post(url);
        ReportFactor.reportResponseHeaders(response, isDeepReporting);
        ReportFactor.reportResponseBody(response, isDeepReporting);
        return ApiResponse.of(response);
    }

    public ApiResponse login(String username, String password) {
        ApiResponse response = this.auth.login(username, password);
        if (response.getResponse().statusCode() == HTTP_OK) {
            this.bearerToken = "Bearer " + response.getResponse().jsonPath().getString(TOKEN_PATH);
        }
        return response;
    }

    public ApiResponse login(ILogin user) {
        return login(user.getUsername(), user.getPassword());
    }

    public void logout() {
        this.bearerToken = null;
    }

    @Override
    public ApiResponse delete(String url, String payload) {
        url = backendUrl + url;
        RequestSpecification request = generateRequestSpecification(payload);
        ReportFactor.reportRequestUrl("DELETE", url);
        ReportFactor.reportRequestHeader(request, isDeepReporting);
        ReportFactor.reportRequestBody(payload, isDeepReporting);
        Response response = request.delete(url);
        ReportFactor.reportResponseHeaders(response, isDeepReporting);
        ReportFactor.reportResponseBody(response, isDeepReporting);
        return ApiResponse.of(response);
    }

    @Override
    public ApiResponse delete(String url) {
        return delete(url, null);
    }

    @Override
    public ApiResponse put(String url, String payload) {
        url = backendUrl + url;
        RequestSpecification request = generateRequestSpecification(payload);
        ReportFactor.reportRequestUrl("PUT", url);
        ReportFactor.reportRequestHeader(request, isDeepReporting);
        ReportFactor.reportRequestBody(payload, isDeepReporting);
        Response response = request.put(url);
        ReportFactor.reportResponseHeaders(response, isDeepReporting);
        ReportFactor.reportResponseBody(response, isDeepReporting);
        return ApiResponse.of(response);
    }

    private RequestSpecification generateRequestSpecification(String payload) {
        RequestSpecification specification = given()
                .config(getConfig())
                .spec(request)
                .header(AUTHORIZATION, this.bearerToken)
                .queryParams(queryMap)
                .pathParams(pathMap)
                .params(paramMap);
        return StringUtils.isEmpty(payload) ? specification : specification.body(payload);
    }

    private RestAssuredConfig getConfig() {
        return RestAssuredConfig.config()
                .httpClient(
                        new HttpClientConfig()
                                .setParam(SOCKET_TIMEOUT, timeout)
                                .setParam(CONNECTION_TIMEOUT, timeout)
                );
    }
}
