package com.simon.core.apidriver;

import com.smile.core.api.ApiResponse;

public interface RestAPI {
    ApiResponse post(String url, String payload);
}
