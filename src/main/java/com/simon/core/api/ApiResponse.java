package com.simon.core.api;

import io.restassured.response.Response;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ApiResponse {
    public static final String MESSAGE_PATH = "message";
    public static final String DATA_PATH = "data";

    private Response response;

    public static ApiResponse of(Response response) {
        return new ApiResponse().setResponse(response);
    }

    public int statusCode() {
        return response.statusCode();
    }

    public <T> T getObjectFromJsonPath(String jsonPath, Class<T> genericType) {
        return response.getBody().jsonPath().getObject(jsonPath, genericType);
    }

    public Object getFromJsonPath(String jsonPath) {
        return response.getBody().jsonPath().get(jsonPath);
    }

    public String getStringFromJsonPath(String jsonPath) {
        return response.getBody().jsonPath().getString(jsonPath);
    }

    public <T> List<T> getListFromJsonPath(String jsonPath, Class<T> genericType) {
        return response.getBody().jsonPath().getList(jsonPath, genericType);
    }

    public String getMessage() {
        return response.getBody().jsonPath().getString(MESSAGE_PATH);
    }

    public <T> T getData(Class<T> dtoType) {
        return response.getBody().jsonPath().getObject(DATA_PATH, dtoType);
    }

    public <T> List<T> getDataList(Class<T> dtoType) {
        return response.getBody().jsonPath().getList(DATA_PATH, dtoType);
    }
}
