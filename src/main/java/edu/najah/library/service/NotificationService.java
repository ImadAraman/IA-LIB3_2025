package edu.najah.library.service;

import edu.najah.library.domain.Loan;
import edu.najah.library.domain.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for sending notifications to users, particularly overdue reminders.
 * 
 * <p>Implements the Observer Design Pattern as a Subject that maintains a list
 * of observers (EmailNotifier, SMSNotifier, etc.) and notifies them when events occur.</p>
 * 
 * <p>US3.1: As an administrator, I want to send reminder emails to users with
 * overdue books so that they are notified.</p>
 * 
 * @author Imad Araman
 * @version 1.0
 */
public class NotificationService implements Subject {
    
    private List<Observer> observers;
    private OverdueDetectionService overdueDetectionService;
    
    /**
     * Constructs a NotificationService with the required dependencies.
     * 
     * @param overdueDetectionService the service for detecting overdue loans
     */
    public NotificationService(OverdueDetectionService overdueDetectionService) {
        if (overdueDetectionService == null) {
            throw new IllegalArgumentException("OverdueDetectionService cannot be null");
        }
        this.observers = new ArrayList<>();
        this.overdueDetectionService = overdueDetectionService;
    }
    
    /**
     * Constructs a NotificationService with the required dependencies and initial observer.
     * 
     * @param overdueDetectionService the service for detecting overdue loans
     * @param initialObserver the initial observer to attach (e.g., EmailNotifier)
     */
    public NotificationService(OverdueDetectionService overdueDetectionService, Observer initialObserver) {
        this(overdueDetectionService);
        if (initialObserver != null) {
            attach(initialObserver);
        }
    }
    
    
    /**
     * Registers an observer to receive notifications.
     * 
     * @param observer the observer to register
     */
    @Override
    public void attach(Observer observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    /**
     * Unregisters an observer so it no longer receives notifications.
     * 
     * @param observer the observer to unregister
     */
    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }
    
    /**
     * Notifies all registered observers about an event.
     * 
     * @param user the user related to the event
     * @param message the notification message
     */
    @Override
    public void notifyObservers(User user, String message) {
        for (Observer observer : observers) {
            observer.notify(user, message);
        }
    }
    
    /**
     * Sends reminder emails to all users with overdue books.
     * 
     * <p>US3.1 Acceptance: Message: "You have n overdue book(s)."</p>
     * 
     * @param currentDate the current date to check for overdue loans
     * @return number of reminder emails sent
     */
    public int sendOverdueReminders(LocalDate currentDate) {
        if (currentDate == null) {
            return 0;
        }
        
        List<Loan> overdueLoans = overdueDetectionService.getOverdueLoans(currentDate);
        
        // Group overdue loans by user
        List<User> usersWithOverdue = overdueLoans.stream()
                .map(Loan::getUser)
                .distinct()
                .collect(Collectors.toList());
        
        int notificationsSent = 0;
        for (User user : usersWithOverdue) {
            if (sendReminderToUser(user, currentDate)) {
                notificationsSent++;
            }
        }
        
        return notificationsSent;
    }
    
    /**
     * Sends a reminder to a specific user about their overdue books.
     * Notifies all registered observers (email, SMS, push, etc.).
     * 
     * @param user the user to send reminder to
     * @param currentDate the current date to check for overdue loans
     * @return true if at least one notification was sent successfully, false otherwise
     */
    public boolean sendReminderToUser(User user, LocalDate currentDate) {
        if (user == null || currentDate == null || user.getEmail() == null) {
            return false;
        }
        
        List<Loan> overdueLoans = overdueDetectionService.getOverdueLoansForUser(user, currentDate);
        
        if (overdueLoans.isEmpty()) {
            return false;
        }
        
        int overdueCount = overdueLoans.size();
        String message = buildReminderMessage(overdueCount);
        
        // Notify all observers using Observer pattern
        boolean notified = false;
        for (Observer observer : observers) {
            if (observer.notify(user, message)) {
                notified = true;
            }
        }
        
        return notified;
    }
    
    /**
     * Builds the reminder message for a user.
     * 
     * <p>US3.1 Acceptance: Message format: "You have n overdue book(s)."</p>
     * 
     * @param overdueCount the number of overdue books
     * @return the formatted reminder message
     */
    private String buildReminderMessage(int overdueCount) {
        if (overdueCount == 1) {
            return "You have 1 overdue book.";
        } else {
            return "You have " + overdueCount + " overdue book(s).";
        }
    }
    
    /**
     * Sends reminder emails to all users with overdue books as of today.
     * 
     * @return number of reminder emails sent
     */
    public int sendOverdueReminders() {
        return sendOverdueReminders(LocalDate.now());
    }
}

