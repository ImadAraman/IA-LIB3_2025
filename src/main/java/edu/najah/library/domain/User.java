package edu.najah.library.domain;

import jakarta.persistence.*;
import java.util.Objects;

/**
 * Represents a library user/patron.
 * Each user has a unique ID, name, and email.
 * 
 * <p>This entity is mapped to the "users" table in the database.</p>
 * 
 * @author Imad Araman, Hamza Abuobaid
 * @version 1.0
 */
@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "user_id")
})
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "user_id", nullable = false, unique = true, length = 50)
    private String userId;
    
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    
    @Column(name = "email", nullable = false, length = 255)
    private String email;
    
    /**
     * Default constructor required by JPA.
     */
    public User() {
    }
    
    /**
     * Gets the user's database ID.
     * 
     * @return the user ID
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Sets the user's database ID.
     * 
     * @param id the ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Constructs a User with specified details.
     * 
     * @param userId the user's unique identifier
     * @param name the user's name
     * @param email the user's email
     */
    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }
    
    /**
     * Gets the user's ID.
     * 
     * @return the user ID
     */
    public String getUserId() {
        return userId;
    }
    
    /**
     * Sets the user's ID.
     * 
     * @param userId the user ID to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    /**
     * Gets the user's name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the user's name.
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Gets the user's email.
     * 
     * @return the email
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Sets the user's email.
     * 
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Compares this user with another object for equality.
     * Two users are equal if they have the same user ID.
     * 
     * @param obj the object to compare with
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(userId, user.userId);
    }
    
    /**
     * Generates a hash code for this user.
     * 
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
    
    /**
     * Returns a string representation of this user.
     * 
     * @return string representation
     */
    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
