package com.simon.core.apidriver;


import com.simon.core.api.ApiResponse;

public interface RestAPI {
    ApiResponse get(String url);

    ApiResponse get(String url, String payload);

    ApiResponse post(String url, String payload);

    ApiResponse delete(String url, String payload);

    ApiResponse delete(String url);

    ApiResponse put(String url, String payload);
}
