package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.*;
import com.techelevator.view.ConsoleService;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
    private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
    private static final String[] LOGIN_MENU_OPTIONS = {LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT};
    private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
    private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
    private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
    private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
    private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
    private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
    private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT};

    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private UserService userService;
    private TransferService transferService;
    private AccountService accountService;

    public static void main(String[] args) {
        App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL), new UserService(),
                new TransferService(), new AccountService());
        app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService, UserService userService, TransferService transferService, AccountService accountService) {
        this.console = console;
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.transferService = transferService;
        this.accountService = accountService;
    }


    public void run() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");

        registerAndLogin();
        mainMenu();
    }

    private void mainMenu() {
        while (true) {
            String choice = (String) console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
            if (MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
                viewCurrentBalance();
            } else if (MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
                viewTransferHistory();
            } else if (MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
                viewPendingRequests();
            } else if (MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
                sendBucks();
            } else if (MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
                requestBucks();
            } else if (MAIN_MENU_OPTION_LOGIN.equals(choice)) {
                login();
            } else {
                // the only other option on the main menu is to exit
                exitProgram();
            }
        }
    }

    private void viewCurrentBalance() {
        System.out.println("Your current balance is: $" + accountService.getBalance());
    }

    private void viewTransferHistory() {
        System.out.println("-------------------------------------------");
        System.out.println("Transfers");
        System.out.println("ID          From/To                 Amount");// spaces 10, 17
        System.out.println("-------------------------------------------");
        boolean isDisplaying = true;
        while (isDisplaying) {
            for (Transfer transfer : transferService.getAllUserTransfers()) {
                //TODO: get the name on the account: id, to/from, name, amt -done?
                if (transfer.getAccount_from().getUsername().equals(currentUser.getUser().getUsername())) {
                    System.out.println(transfer.getTransfer_id() + "        To:"
                            + transfer.getAccount_to().getUsername() + "                "
                            + transfer.getAmount());
                } else {
                    System.out.println(transfer.getTransfer_id() + "        From:"
                            + transfer.getAccount_from().getUsername() + "                "
                            + transfer.getAmount());
                }
            }
            System.out.println("-------------------------------------------");
            int transferId = console.getUserInputInteger("Please enter transfer ID to view details (0 to cancel)");
            if (transferId == 0) {
                isDisplaying = false;
            } else {
                boolean validTransferId = false;
                for (Transfer transfer : transferService.getAllUserTransfers()) {
                    if (transfer.getTransfer_id() == transferId) {
                        validTransferId = true;
                        boolean wantToExit = false;
                        while (!wantToExit) {
                            System.out.println("-------------------------------------------");
                            System.out.println("Transfer Details");
                            System.out.println("-------------------------------------------");
                            System.out.println(transfer);
                            if (console.getUserInput("Push Enter to Continue").equals("")) {
                                wantToExit = true;
                            }
                        }

                    }
                }
                if (!validTransferId) {
                    System.out.println("Please enter a valid transfer Id\n");
                }
            }
        }
    }

    private void viewPendingRequests() {
		System.out.println("-------------------------------------------");
		System.out.println("Pending Transfers");
		System.out.println("ID          To                      Amount");// spaces 10, 17
		System.out.println("-------------------------------------------");
		boolean isDisplaying = true;
		while (isDisplaying) {
			for (Transfer transfer : transferService.getAllUserTransfers()) {
				//TODO: get the name on the account: id, to/from, name, amt -done?
				if (transfer.getAccount_from().getUsername().equals(currentUser.getUser().getUsername())
						&& transfer.getTransfer_status_desc().equals("Pending")
						&& transfer.getTransfer_type_desc().equals("Request")) {
					System.out.println(transfer.getTransfer_id() + "           "
							+ transfer.getAccount_to().getUsername() + "                "
							+ transfer.getAmount());
				}
			}
			System.out.println("-------------------------------------------");
			int transferId = console.getUserInputInteger("Please enter transfer ID to approve/reject (0 to cancel)");
			if (transferId == 0) {
				isDisplaying = false;
			} else {
				boolean validTransferId = false;
				for (Transfer transfer : transferService.getAllUserTransfers()) {
					if (transfer.getTransfer_id() == transferId) {
						validTransferId = true;
						boolean wantToExit = false;
						while (!wantToExit) {
							System.out.println("-------------------------------------------");
							System.out.println("1: Approve");
							System.out.println("2: Reject");
							System.out.println("0: Don't approve or reject");
							System.out.println("-------------------------------------------");
							String userChoice = console.getUserInput("Please choose an option");
							//TODO finish?
//							System.out.println(transfer);
//							if (console.getUserInput("Push Enter to Continue").equals("")) {
//								wantToExit = true;
//							}
						}

					}
				}
				if (!validTransferId) {
					System.out.println("Please enter a valid transfer Id\n");
				}
			}
		}
	}


    private void sendBucks() {
        // TODO Auto-generated method stub
        System.out.println("-------------------------------------------");
        System.out.println("Users");
        System.out.println("ID          Name");
        System.out.println("-------------------------------------------");
        boolean isSent = false;
        while (!isSent) {
            for (User user : userService.listAllUsers()) {
                if (!user.getUsername().equals(currentUser.getUser().getUsername())) {
                    System.out.println(user.getId() + "        " + user.getUsername());
                }
            }
            System.out.println("-------------------------------------------");
            int userId = console.getUserInputInteger("Enter ID of user you are sending to (0 to cancel)");
            if (userId == 0) {
                isSent = true;
            } else {
                boolean userFound = false;
                for (User user : userService.listAllUsers()) {
                    if (user.getId() == userId) {
                        userFound = true;
                        double transferAmount = Double.parseDouble(console.getUserInput("Enter amount"));
                        if (transferAmount > 0) {
                            Transfer transferToDisplay = transferService.transferFunds(transferAmount, user.getAccount_id());
                            System.out.println("-------------------------------------------");
                            System.out.println(transferToDisplay);
                            isSent = true;
                        } else {
                            System.out.println("Please enter a valid amount above zero.\n");
                        }
                    }
                }
                if (!userFound) {
                    System.out.println("Please enter valid userId\n");
                }
            }
        }
    }

    private void requestBucks() {
        // TODO Auto-generated method stub

    }

    private void exitProgram() {
        System.exit(0);
    }

    private void registerAndLogin() {
        while (!isAuthenticated()) {
            String choice = (String) console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
            if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
                login();
            } else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
                register();
            } else {
                // the only other option on the login menu is to exit
                exitProgram();
            }
        }
    }

    private boolean isAuthenticated() {
        return currentUser != null;
    }

    private void register() {
        System.out.println("Please register a new user account");
        boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
                authenticationService.register(credentials);
                isRegistered = true;
                System.out.println("Registration successful. You can now login.");
            } catch (AuthenticationServiceException e) {
                System.out.println("REGISTRATION ERROR: " + e.getMessage());
                System.out.println("Please attempt to register again.");
            }
        }
    }

    private void login() {
        System.out.println("Please log in");
        currentUser = null;
        while (currentUser == null) //will keep looping until user is logged in
        {
            UserCredentials credentials = collectUserCredentials();
            try {
                currentUser = authenticationService.login(credentials);
                userService.setAuthUser(currentUser);
                transferService.setAuthUser(currentUser);
                accountService.setAuthUser(currentUser);
            } catch (AuthenticationServiceException e) {
                System.out.println("LOGIN ERROR: " + e.getMessage());
                System.out.println("Please attempt to login again.");
            }
        }
    }

    private UserCredentials collectUserCredentials() {
        String username = console.getUserInput("Username");
        String password = console.getUserInput("Password");
        return new UserCredentials(username, password);
    }
}
