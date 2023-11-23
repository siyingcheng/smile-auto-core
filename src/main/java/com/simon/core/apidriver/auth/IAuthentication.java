package com.simon.core.apidriver.auth;


import com.simon.core.api.ApiResponse;
import com.simon.core.config.Configurator;

public interface IAuthentication {
    ApiResponse login(String username, String password);

    Configurator configurator();
}
