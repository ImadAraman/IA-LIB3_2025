package edu.najah.library.service;

/**
 * Service interface for sending emails.
 * Provides abstraction for email operations to allow mocking in tests.
 * 
 * @author Imad Araman
 * @version 1.0
 */
public interface EmailService {
    
    /**
     * Sends an email to the specified recipient.
     * 
     * @param to the recipient email address
     * @param subject the email subject
     * @param message the email message body
     * @return true if email was sent successfully, false otherwise
     */
    boolean sendEmail(String to, String subject, String message);
    
    /**
     * Checks if this email service is in test mode.
     * In test mode, emails are recorded rather than actually sent.
     * 
     * @return true if in test mode, false otherwise
     */
    boolean isTestMode();
}

