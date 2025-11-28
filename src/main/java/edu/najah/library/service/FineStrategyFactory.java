package edu.najah.library.service;

/**
 * Factory class for creating fine calculation strategies based on item type.
 * 
 * <p>Provides a centralized way to get the appropriate FineStrategy implementation
 * for different library item types.</p>
 * 
 * @author Imad Araman
 * @version 1.0
 */
public class FineStrategyFactory {
    
    /**
     * Enumeration of library item types.
     */
    public enum ItemType {
        BOOK,
        CD,
        JOURNAL
    }
    
    /**
     * Gets the appropriate fine strategy for the given item type.
     * 
     * @param itemType the type of library item
     * @return the fine strategy for that item type
     * @throws IllegalArgumentException if itemType is null
     */
    public static FineStrategy getStrategy(ItemType itemType) {
        if (itemType == null) {
            throw new IllegalArgumentException("Item type cannot be null");
        }
        
        switch (itemType) {
            case BOOK:
                return new BookFineStrategy();
            case CD:
                return new CDFineStrategy();
            case JOURNAL:
                return new JournalFineStrategy();
            default:
                throw new IllegalArgumentException("Unknown item type: " + itemType);
        }
    }
    
    /**
     * Gets the default fine strategy for books.
     * This maintains backward compatibility with existing code.
     * 
     * @return the book fine strategy
     */
    public static FineStrategy getDefaultStrategy() {
        return new BookFineStrategy();
    }
}

