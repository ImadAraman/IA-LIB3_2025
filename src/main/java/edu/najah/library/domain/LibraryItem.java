package edu.najah.library.domain;

/**
 * Interface for all library items that can be borrowed.
 * 
 * <p>Implements polymorphism to allow different types of library items
 * (books, CDs, journals) to be handled uniformly while maintaining
 * their specific behaviors (loan periods, fine rates).</p>
 * 
 * @author Imad Araman
 * @version 1.0
 */
public interface LibraryItem {
    
    /**
     * Gets the unique identifier for this item.
     * 
     * @return the unique identifier (ISBN for books, catalog number for CDs, etc.)
     */
    String getUniqueIdentifier();
    
    /**
     * Gets the title of this item.
     * 
     * @return the title
     */
    String getTitle();
    
    /**
     * Checks if this item is available for borrowing.
     * 
     * @return true if available, false otherwise
     */
    boolean isAvailable();
    
    /**
     * Sets the availability status of this item.
     * 
     * @param available the availability status
     */
    void setAvailable(boolean available);
    
    /**
     * Gets the loan period in days for this item type.
     * 
     * @return the loan period in days
     */
    int getLoanPeriodDays();
    
    /**
     * Gets the type of this library item.
     * 
     * @return the item type (BOOK, CD, JOURNAL, etc.)
     */
    ItemType getItemType();
    
    /**
     * Enumeration of library item types.
     */
    enum ItemType {
        BOOK,
        CD,
        JOURNAL
    }
}

