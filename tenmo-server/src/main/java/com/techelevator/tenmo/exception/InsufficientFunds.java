package com.techelevator.tenmo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( code = HttpStatus.NOT_FOUND, reason = "Insufficient funds. Transfer cannot be larger than balance.")
public class InsufficientFunds extends Exception {
    private static final long serialVersionUID = 1L;

    public InsufficientFunds() {
        super("Insufficient funds. Transfer cannot be larger than balance.");
    }
}
