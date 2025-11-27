package edu.najah.library.domain;

import java.util.Objects;

/**
 * Represents a journal in the library management system.
 * Each journal has a title, publisher, ISSN, and availability status.
 * 
 * @author Imad Araman
 * @version 1.0
 */
public class Journal {
    
    private String title;
    private String publisher;
    private String issn;
    private boolean isAvailable;
    
    /**
     * Default constructor.
     */
    public Journal() {
        this.isAvailable = true; // New journals are available by default
    }
    
    /**
     * Constructs a Journal with specified details.
     * Journal is available by default.
     * 
     * @param title the journal's title
     * @param publisher the journal's publisher
     * @param issn the journal's ISSN
     */
    public Journal(String title, String publisher, String issn) {
        this.title = title;
        this.publisher = publisher;
        this.issn = issn;
        this.isAvailable = true;
    }
    
    /**
     * Constructs a Journal with specified details and availability.
     * 
     * @param title the journal's title
     * @param publisher the journal's publisher
     * @param issn the journal's ISSN
     * @param isAvailable the journal's availability status
     */
    public Journal(String title, String publisher, String issn, boolean isAvailable) {
        this.title = title;
        this.publisher = publisher;
        this.issn = issn;
        this.isAvailable = isAvailable;
    }
    
    /**
     * Gets the journal's title.
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Sets the journal's title.
     * 
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Gets the journal's publisher.
     * 
     * @return the publisher
     */
    public String getPublisher() {
        return publisher;
    }
    
    /**
     * Sets the journal's publisher.
     * 
     * @param publisher the publisher to set
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    
    /**
     * Gets the journal's ISSN.
     * 
     * @return the ISSN
     */
    public String getIssn() {
        return issn;
    }
    
    /**
     * Sets the journal's ISSN.
     * 
     * @param issn the ISSN to set
     */
    public void setIssn(String issn) {
        this.issn = issn;
    }
    
    /**
     * Checks if the journal is available for borrowing.
     * 
     * @return true if available, false otherwise
     */
    public boolean isAvailable() {
        return isAvailable;
    }
    
    /**
     * Sets the journal's availability status.
     * 
     * @param available the availability status to set
     */
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
    
    /**
     * Compares this journal with another object for equality.
     * Two journals are equal if they have the same ISSN.
     * 
     * @param obj the object to compare with
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Journal journal = (Journal) obj;
        return Objects.equals(issn, journal.issn);
    }
    
    /**
     * Generates a hash code for this journal.
     * 
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(issn);
    }
    
    /**
     * Returns a string representation of this journal.
     * 
     * @return string representation
     */
    @Override
    public String toString() {
        return "Journal{" +
                "title='" + title + '\'' +
                ", publisher='" + publisher + '\'' +
                ", issn='" + issn + '\'' +
                ", isAvailable=" + isAvailable +
                '}';
    }
}

