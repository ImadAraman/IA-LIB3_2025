package edu.najah.library.service;

import edu.najah.library.domain.User;

/**
 * Observer interface for notification services.
 * 
 * <p>Implements the Observer Design Pattern to allow multiple notification channels
 * (email, SMS, push notifications) without modifying the core notification logic.</p>
 * 
 * <p>Observers can be registered with a subject (NotificationService) and will be
 * notified when events occur (e.g., overdue reminders).</p>
 * 
 * @author Imad Araman
 * @version 1.0
 */
public interface Observer {
    
    /**
     * Notifies the observer about an event for a user.
     * 
     * @param user the user to notify
     * @param message the notification message
     * @return true if notification was sent successfully, false otherwise
     */
    boolean notify(User user, String message);
    
    /**
     * Gets the type/name of this observer (e.g., "Email", "SMS", "Push").
     * 
     * @return the observer type
     */
    String getObserverType();
}

