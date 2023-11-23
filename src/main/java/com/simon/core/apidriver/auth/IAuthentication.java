package com.simon.core.apidriver.auth;

import com.smile.core.api.ApiResponse;
import com.smile.core.config.Configurator;

public interface IAuthentication {
    ApiResponse login(String username, String password);

    Configurator configurator();
}
