package edu.najah.library.service;

/**
 * Fine calculation strategy for journals.
 * Journals have a fine rate of 15 NIS per day.
 * 
 * <p>Implements the Strategy Design Pattern for fine calculation.</p>
 * 
 * @author Imad Araman
 * @version 1.0
 */
public class JournalFineStrategy implements FineStrategy {
    
    private static final int JOURNAL_FINE_RATE = 15; // NIS per day
    
    /**
     * Calculates the fine for a journal based on overdue days.
     * 
     * @param overdueDays the number of days the journal is overdue
     * @return the calculated fine amount in NIS
     */
    @Override
    public int calculateFine(int overdueDays) {
        if (overdueDays <= 0) {
            return 0;
        }
        return overdueDays * JOURNAL_FINE_RATE;
    }
    
    /**
     * Gets the fine rate per day for journals.
     * 
     * @return the fine rate per day (15 NIS)
     */
    @Override
    public int getFineRatePerDay() {
        return JOURNAL_FINE_RATE;
    }
}

