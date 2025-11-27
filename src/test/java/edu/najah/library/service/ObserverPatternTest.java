package edu.najah.library.service;

import edu.najah.library.domain.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Observer Design Pattern implementation.
 * Tests Observer interface, EmailNotifier, and NotificationService as Subject.
 * 
 * @author Imad Araman
 * @version 1.0
 */
public class ObserverPatternTest {
    
    private MockEmailServer emailServer;
    private EmailNotifier emailNotifier;
    private NotificationService notificationService;
    private OverdueDetectionService overdueDetectionService;
    private User user;
    
    @BeforeEach
    public void setUp() {
        emailServer = new MockEmailServer(true);
        emailNotifier = new EmailNotifier(emailServer, "Test Subject");
        BorrowingService borrowingService = new BorrowingService();
        overdueDetectionService = new OverdueDetectionService(borrowingService);
        notificationService = new NotificationService(overdueDetectionService);
        user = new User("U001", "John Doe", "john@example.com");
    }
    
    @Test
    @DisplayName("Observer Pattern: EmailNotifier implements Observer interface")
    public void testEmailNotifierImplementsObserver() {
        assertTrue(emailNotifier instanceof Observer);
        assertEquals("Email", emailNotifier.getObserverType());
    }
    
    @Test
    @DisplayName("Observer Pattern: EmailNotifier notifies user via email")
    public void testEmailNotifierNotify() {
        boolean result = emailNotifier.notify(user, "Test message");
        
        assertTrue(result);
        assertEquals(1, emailServer.getSentEmailCount());
        
        MockEmailServer.EmailRecord email = emailServer.getSentEmails().get(0);
        assertEquals("john@example.com", email.getTo());
        assertEquals("Test Subject", email.getSubject());
        assertEquals("Test message", email.getMessage());
    }
    
    @Test
    @DisplayName("Observer Pattern: EmailNotifier with custom subject")
    public void testEmailNotifierCustomSubject() {
        boolean result = emailNotifier.notify(user, "Custom Subject", "Custom message");
        
        assertTrue(result);
        MockEmailServer.EmailRecord email = emailServer.getSentEmails().get(0);
        assertEquals("Custom Subject", email.getSubject());
        assertEquals("Custom message", email.getMessage());
    }
    
    @Test
    @DisplayName("Observer Pattern: EmailNotifier returns false for null user")
    public void testEmailNotifierNullUser() {
        assertFalse(emailNotifier.notify(null, "Message"));
        assertEquals(0, emailServer.getSentEmailCount());
    }
    
    @Test
    @DisplayName("Observer Pattern: NotificationService implements Subject interface")
    public void testNotificationServiceImplementsSubject() {
        assertTrue(notificationService instanceof Subject);
    }
    
    @Test
    @DisplayName("Observer Pattern: NotificationService can attach and detach observers")
    public void testAttachDetachObservers() {
        assertEquals(0, emailServer.getSentEmailCount());
        
        // Attach observer
        notificationService.attach(emailNotifier);
        notificationService.notifyObservers(user, "Test message");
        assertEquals(1, emailServer.getSentEmailCount());
        
        // Detach observer
        notificationService.detach(emailNotifier);
        emailServer.clearSentEmails();
        notificationService.notifyObservers(user, "Test message");
        assertEquals(0, emailServer.getSentEmailCount());
    }
    
    @Test
    @DisplayName("Observer Pattern: NotificationService notifies all attached observers")
    public void testNotifyAllObservers() {
        MockEmailServer emailServer2 = new MockEmailServer(true);
        EmailNotifier emailNotifier2 = new EmailNotifier(emailServer2, "Subject 2");
        
        notificationService.attach(emailNotifier);
        notificationService.attach(emailNotifier2);
        
        notificationService.notifyObservers(user, "Test message");
        
        assertEquals(1, emailServer.getSentEmailCount());
        assertEquals(1, emailServer2.getSentEmailCount());
    }
    
    @Test
    @DisplayName("Observer Pattern: Multiple observers can be attached")
    public void testMultipleObservers() {
        MockEmailServer emailServer2 = new MockEmailServer(true);
        MockEmailServer emailServer3 = new MockEmailServer(true);
        
        EmailNotifier notifier1 = new EmailNotifier(emailServer, "Subject 1");
        EmailNotifier notifier2 = new EmailNotifier(emailServer2, "Subject 2");
        EmailNotifier notifier3 = new EmailNotifier(emailServer3, "Subject 3");
        
        notificationService.attach(notifier1);
        notificationService.attach(notifier2);
        notificationService.attach(notifier3);
        
        notificationService.notifyObservers(user, "Multi-observer message");
        
        assertEquals(1, emailServer.getSentEmailCount());
        assertEquals(1, emailServer2.getSentEmailCount());
        assertEquals(1, emailServer3.getSentEmailCount());
    }
    
    @Test
    @DisplayName("Observer Pattern: Constructor with initial observer")
    public void testConstructorWithInitialObserver() {
        NotificationService service = new NotificationService(overdueDetectionService, emailNotifier);
        
        service.notifyObservers(user, "Message");
        assertEquals(1, emailServer.getSentEmailCount());
    }
    
    @Test
    @DisplayName("Observer Pattern: EmailNotifier constructor throws exception for null EmailService")
    public void testEmailNotifierNullEmailService() {
        assertThrows(IllegalArgumentException.class, 
            () -> new EmailNotifier(null));
    }
    
    /**
     * Mock observer for testing multiple observer types.
     */
    private static class MockObserver implements Observer {
        private List<String> notifications = new ArrayList<>();
        private String type;
        
        public MockObserver(String type) {
            this.type = type;
        }
        
        @Override
        public boolean notify(User user, String message) {
            notifications.add(message);
            return true;
        }
        
        @Override
        public String getObserverType() {
            return type;
        }
        
        public List<String> getNotifications() {
            return notifications;
        }
    }
    
    @Test
    @DisplayName("Observer Pattern: Supports different observer types (mock SMS)")
    public void testDifferentObserverTypes() {
        MockObserver smsObserver = new MockObserver("SMS");
        MockObserver pushObserver = new MockObserver("Push");
        
        notificationService.attach(emailNotifier);
        notificationService.attach(smsObserver);
        notificationService.attach(pushObserver);
        
        notificationService.notifyObservers(user, "Multi-channel message");
        
        assertEquals(1, emailServer.getSentEmailCount());
        assertEquals(1, smsObserver.getNotifications().size());
        assertEquals(1, pushObserver.getNotifications().size());
        assertEquals("Multi-channel message", smsObserver.getNotifications().get(0));
    }
}

