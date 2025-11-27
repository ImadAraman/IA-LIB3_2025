package edu.najah.library.service;

/**
 * Subject interface for the Observer Design Pattern.
 * 
 * <p>A Subject maintains a list of observers and notifies them when state changes occur.</p>
 * 
 * @author Imad Araman
 * @version 1.0
 */
public interface Subject {
    
    /**
     * Registers an observer to receive notifications.
     * 
     * @param observer the observer to register
     */
    void attach(Observer observer);
    
    /**
     * Unregisters an observer so it no longer receives notifications.
     * 
     * @param observer the observer to unregister
     */
    void detach(Observer observer);
    
    /**
     * Notifies all registered observers about an event.
     * 
     * @param user the user related to the event
     * @param message the notification message
     */
    void notifyObservers(edu.najah.library.domain.User user, String message);
}

