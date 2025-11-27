package edu.najah.library.service;

import edu.najah.library.domain.User;

/**
 * Email notification observer implementation.
 * 
 * <p>Implements the Observer Design Pattern for email notifications.
 * Sends notifications via email using the EmailService.</p>
 * 
 * @author Imad Araman
 * @version 1.0
 */
public class EmailNotifier implements Observer {
    
    private EmailService emailService;
    private String defaultSubject;
    
    /**
     * Constructs an EmailNotifier with the specified email service.
     * 
     * @param emailService the email service to use for sending emails
     */
    public EmailNotifier(EmailService emailService) {
        if (emailService == null) {
            throw new IllegalArgumentException("EmailService cannot be null");
        }
        this.emailService = emailService;
        this.defaultSubject = "Library Notification";
    }
    
    /**
     * Constructs an EmailNotifier with the specified email service and subject.
     * 
     * @param emailService the email service to use for sending emails
     * @param defaultSubject the default subject for email notifications
     */
    public EmailNotifier(EmailService emailService, String defaultSubject) {
        if (emailService == null) {
            throw new IllegalArgumentException("EmailService cannot be null");
        }
        this.emailService = emailService;
        this.defaultSubject = defaultSubject != null ? defaultSubject : "Library Notification";
    }
    
    /**
     * Notifies the user via email.
     * 
     * @param user the user to notify
     * @param message the notification message
     * @return true if email was sent successfully, false otherwise
     */
    @Override
    public boolean notify(User user, String message) {
        if (user == null || user.getEmail() == null || message == null) {
            return false;
        }
        
        return emailService.sendEmail(user.getEmail(), defaultSubject, message);
    }
    
    /**
     * Notifies the user via email with a custom subject.
     * 
     * @param user the user to notify
     * @param subject the email subject
     * @param message the notification message
     * @return true if email was sent successfully, false otherwise
     */
    public boolean notify(User user, String subject, String message) {
        if (user == null || user.getEmail() == null || message == null) {
            return false;
        }
        
        String emailSubject = subject != null ? subject : defaultSubject;
        return emailService.sendEmail(user.getEmail(), emailSubject, message);
    }
    
    /**
     * Gets the observer type.
     * 
     * @return "Email"
     */
    @Override
    public String getObserverType() {
        return "Email";
    }
}

