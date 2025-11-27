package edu.najah.library.service;

import edu.najah.library.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing user registration and unregistration.
 * 
 * <p>US4.2: As an administrator, I want to unregister a user so that inactive
 * accounts are removed.</p>
 * 
 * @author Imad Araman
 * @version 1.0
 */
public class UserManagementService {
    
    private List<User> registeredUsers;
    private BorrowingService borrowingService;
    private AuthenticationService authenticationService;
    private EmailService emailService;
    
    /**
     * Constructs a UserManagementService with the required dependencies.
     * 
     * @param borrowingService the borrowing service to check for active loans and fines
     * @param authenticationService the authentication service to verify admin privileges
     * @param emailService the email service to send unregistration notifications
     */
    public UserManagementService(BorrowingService borrowingService, 
                                 AuthenticationService authenticationService,
                                 EmailService emailService) {
        if (borrowingService == null || authenticationService == null || emailService == null) {
            throw new IllegalArgumentException("Dependencies cannot be null");
        }
        this.registeredUsers = new ArrayList<>();
        this.borrowingService = borrowingService;
        this.authenticationService = authenticationService;
        this.emailService = emailService;
    }
    
    /**
     * Registers a new user in the system.
     * 
     * @param user the user to register
     * @return true if user was registered successfully, false if user already exists
     */
    public boolean registerUser(User user) {
        if (user == null || user.getUserId() == null) {
            return false;
        }
        
        // Check if user already exists
        if (isUserRegistered(user.getUserId())) {
            return false;
        }
        
        registeredUsers.add(user);
        return true;
    }
    
    /**
     * Unregisters a user from the system.
     * Only admins can unregister users.
     * Users with active loans or unpaid fines cannot be unregistered.
     * 
     * <p>US4.2 Acceptance:
     * - Only admins can unregister
     * - Users with active loans or unpaid fines cannot be unregistered</p>
     * 
     * @param userId the ID of the user to unregister
     * @throws IllegalStateException if admin is not logged in
     * @throws IllegalArgumentException if user cannot be unregistered due to active loans or unpaid fines
     * @return true if user was unregistered successfully, false if user not found
     */
    public boolean unregisterUser(String userId) {
        // Check if admin is logged in
        if (!authenticationService.isLoggedIn()) {
            throw new IllegalStateException("Only administrators can unregister users.");
        }
        
        if (userId == null) {
            return false;
        }
        
        User user = findUserById(userId);
        if (user == null) {
            return false;
        }
        
        // Check for active loans
        List<edu.najah.library.domain.Loan> activeLoans = borrowingService.getActiveLoans(user);
        if (!activeLoans.isEmpty()) {
            throw new IllegalArgumentException("Cannot unregister user with active loans. Please return all borrowed books first.");
        }
        
        // Check for unpaid fines
        if (borrowingService.hasUnpaidFines(user)) {
            throw new IllegalArgumentException("Cannot unregister user with unpaid fines. Please pay all fines first.");
        }
        
        // Remove user
        boolean removed = registeredUsers.remove(user);
        
        if (removed && user.getEmail() != null) {
            // Send unregistration notification email
            sendUnregistrationNotification(user);
        }
        
        return removed;
    }
    
    /**
     * Checks if a user is registered.
     * 
     * @param userId the user ID to check
     * @return true if user is registered, false otherwise
     */
    public boolean isUserRegistered(String userId) {
        return findUserById(userId) != null;
    }
    
    /**
     * Finds a user by their ID.
     * 
     * @param userId the user ID
     * @return the user if found, null otherwise
     */
    public User findUserById(String userId) {
        if (userId == null) {
            return null;
        }
        
        return registeredUsers.stream()
                .filter(user -> userId.equals(user.getUserId()))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Gets all registered users.
     * 
     * @return list of all registered users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(registeredUsers);
    }
    
    /**
     * Sends an unregistration notification email to the user.
     * 
     * @param user the unregistered user
     */
    private void sendUnregistrationNotification(User user) {
        if (user.getEmail() == null) {
            return;
        }
        
        String subject = "Account Unregistration Confirmation";
        String message = "Dear " + user.getName() + ",\n\n" +
                        "Your library account has been unregistered. " +
                        "If you have any questions, please contact the library.\n\n" +
                        "Thank you.";
        
        emailService.sendEmail(user.getEmail(), subject, message);
    }
}

