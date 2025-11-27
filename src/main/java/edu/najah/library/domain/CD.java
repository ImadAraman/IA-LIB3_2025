package edu.najah.library.domain;

import java.util.Objects;

/**
 * Represents a CD in the library management system.
 * Each CD has a title, artist, and availability status.
 * 
 * @author Imad Araman
 * @version 1.0
 */
public class CD {
    
    private String title;
    private String artist;
    private String catalogNumber;
    private boolean isAvailable;
    
    /**
     * Default constructor.
     */
    public CD() {
        this.isAvailable = true; // New CDs are available by default
    }
    
    /**
     * Constructs a CD with specified details.
     * CD is available by default.
     * 
     * @param title the CD's title
     * @param artist the CD's artist
     * @param catalogNumber the CD's catalog number
     */
    public CD(String title, String artist, String catalogNumber) {
        this.title = title;
        this.artist = artist;
        this.catalogNumber = catalogNumber;
        this.isAvailable = true;
    }
    
    /**
     * Constructs a CD with specified details and availability.
     * 
     * @param title the CD's title
     * @param artist the CD's artist
     * @param catalogNumber the CD's catalog number
     * @param isAvailable the CD's availability status
     */
    public CD(String title, String artist, String catalogNumber, boolean isAvailable) {
        this.title = title;
        this.artist = artist;
        this.catalogNumber = catalogNumber;
        this.isAvailable = isAvailable;
    }
    
    /**
     * Gets the CD's title.
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Sets the CD's title.
     * 
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Gets the CD's artist.
     * 
     * @return the artist
     */
    public String getArtist() {
        return artist;
    }
    
    /**
     * Sets the CD's artist.
     * 
     * @param artist the artist to set
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }
    
    /**
     * Gets the CD's catalog number.
     * 
     * @return the catalog number
     */
    public String getCatalogNumber() {
        return catalogNumber;
    }
    
    /**
     * Sets the CD's catalog number.
     * 
     * @param catalogNumber the catalog number to set
     */
    public void setCatalogNumber(String catalogNumber) {
        this.catalogNumber = catalogNumber;
    }
    
    /**
     * Checks if the CD is available for borrowing.
     * 
     * @return true if available, false otherwise
     */
    public boolean isAvailable() {
        return isAvailable;
    }
    
    /**
     * Sets the CD's availability status.
     * 
     * @param available the availability status to set
     */
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
    
    /**
     * Compares this CD with another object for equality.
     * Two CDs are equal if they have the same catalog number.
     * 
     * @param obj the object to compare with
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CD cd = (CD) obj;
        return Objects.equals(catalogNumber, cd.catalogNumber);
    }
    
    /**
     * Generates a hash code for this CD.
     * 
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(catalogNumber);
    }
    
    /**
     * Returns a string representation of this CD.
     * 
     * @return string representation
     */
    @Override
    public String toString() {
        return "CD{" +
                "title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", catalogNumber='" + catalogNumber + '\'' +
                ", isAvailable=" + isAvailable +
                '}';
    }
}

