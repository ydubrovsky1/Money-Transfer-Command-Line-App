package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.UserDoesNotExist;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import java.security.Principal;

@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {
    AccountDao accountDao;
    UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @RequestMapping(path = "/balance")
    public double getBalance(Principal principal) throws UserDoesNotExist {
        return accountDao.getBalance(userDao.findIdByUsername(principal.getName()));
    }
}
