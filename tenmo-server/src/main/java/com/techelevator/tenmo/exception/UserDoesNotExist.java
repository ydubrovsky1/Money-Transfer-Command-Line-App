package com.techelevator.tenmo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( code = HttpStatus.NOT_FOUND, reason = "User not found.")
public class UserDoesNotExist extends Exception {
    private static final long serialVersionUID = 1L;

    public UserDoesNotExist() {
        super("User not found.");
    }
}