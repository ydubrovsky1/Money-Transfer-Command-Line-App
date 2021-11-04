package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.InsufficientFunds;
import com.techelevator.tenmo.exception.UserDoesNotExist;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@PreAuthorize("isAuthenticated()")
@RestController
public class TransferController {
    private TransferDao transferDao;
    private UserDao userDao;
    private AccountDao accountDao;

    public TransferController(TransferDao transferDao, UserDao userDao, AccountDao accountDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
        this.accountDao = accountDao;
    }

    @RequestMapping(value = "/transfer/{id}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int id){
        return transferDao.getTransferById(id);
    }

    //TODO test after accountDAO.getAccountByUserId is implemented
    @RequestMapping(value = "/transfer/account", method = RequestMethod.GET)
    public List<Transfer> getTransferByAccountId(Principal principal) throws UserDoesNotExist {
        return transferDao.getAllUserTransfers(
                accountDao.getAccountByUserName(principal.getName()).getAccount_id()
        ); //userDao.findIdByUsername(principal.getName())
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public Transfer transferFunds(
            //FIXME changed to take in request body as transfer
            Principal principal, @Valid @RequestBody Transfer returnTransfer
    ) throws UserDoesNotExist, InsufficientFunds {
        return transferDao.transferFunds(returnTransfer);
    }
}
