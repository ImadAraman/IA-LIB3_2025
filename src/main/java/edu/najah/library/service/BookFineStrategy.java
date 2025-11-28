package edu.najah.library.service;

/**
 * Fine calculation strategy for books.
 * Books have a fine rate of 10 NIS per day.
 * 
 * <p>Implements the Strategy Design Pattern for fine calculation.</p>
 * 
 * @author Imad Araman
 * @version 1.0
 */
public class BookFineStrategy implements FineStrategy {
    
    private static final int BOOK_FINE_RATE = 10; // NIS per day
    
    /**
     * Calculates the fine for a book based on overdue days.
     * 
     * @param overdueDays the number of days the book is overdue
     * @return the calculated fine amount in NIS
     */
    @Override
    public int calculateFine(int overdueDays) {
        if (overdueDays <= 0) {
            return 0;
        }
        return overdueDays * BOOK_FINE_RATE;
    }
    
    /**
     * Gets the fine rate per day for books.
     * 
     * @return the fine rate per day (10 NIS)
     */
    @Override
    public int getFineRatePerDay() {
        return BOOK_FINE_RATE;
    }
}

