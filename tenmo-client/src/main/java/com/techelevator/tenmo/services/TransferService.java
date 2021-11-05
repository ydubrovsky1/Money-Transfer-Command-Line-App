package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import org.springframework.web.client.RestTemplate;

public class TransferService {
    private static final String API_BASE_URL = "http://localhost:8080";
    private final RestTemplate restTemplate = new RestTemplate();

    private AuthenticatedUser authUser = null;

    public void setAuthUser (AuthenticatedUser authUser){
        this.authUser = authUser;
    }


}
