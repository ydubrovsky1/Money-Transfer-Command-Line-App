package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferService {
    private static final String API_BASE_URL = "http://localhost:8080";
    private final RestTemplate restTemplate = new RestTemplate();

    private AuthenticatedUser authUser = null;

    public void setAuthUser(AuthenticatedUser authUser){
        this.authUser = authUser;
    }


    public Transfer getTransferById(int transferId){
        Transfer transfer = null;
        try{

            transfer = restTemplate.getForObject(
                    API_BASE_URL +"/transfer/" +transferId, Transfer.class, makeAuthEntity()
            );
        } catch (RestClientResponseException | ResourceAccessException e){
            System.out.println(e.getMessage());
        }
        return transfer;
    }

    public Transfer[] getAllUserTransfers(){
        Transfer[] transfers = null;
        try{
            ResponseEntity<Transfer[]> response  = restTemplate.exchange(
                    API_BASE_URL + "/transfer/account", HttpMethod.GET, makeAuthEntity(), Transfer[].class
            );
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e){
            System.out.println(e.getMessage());
        }
        return transfers;
    }

    public Transfer transferFunds(double amount, int accountToTransferTo) {
        Transfer transfer = new Transfer();
        transfer.setAccount_to_id(accountToTransferTo);
        transfer.setAmount(amount);
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(
                    API_BASE_URL + "/transfer", HttpMethod.POST, makeAuthEntityForPost(transfer), Transfer.class
            );
            transfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return transfer;
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authUser.getToken());
        return new HttpEntity<>(headers);
    }

    private HttpEntity<Transfer> makeAuthEntityForPost(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authUser.getToken());
        return new HttpEntity<>(transfer, headers);
    }
}
