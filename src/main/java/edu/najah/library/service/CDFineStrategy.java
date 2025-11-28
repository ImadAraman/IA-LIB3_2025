package edu.najah.library.service;

/**
 * Fine calculation strategy for CDs.
 * CDs have a fine rate of 20 NIS per day.
 * 
 * <p>Implements the Strategy Design Pattern for fine calculation.</p>
 * 
 * @author Imad Araman
 * @version 1.0
 */
public class CDFineStrategy implements FineStrategy {
    
    private static final int CD_FINE_RATE = 20; // NIS per day
    
    /**
     * Calculates the fine for a CD based on overdue days.
     * 
     * @param overdueDays the number of days the CD is overdue
     * @return the calculated fine amount in NIS
     */
    @Override
    public int calculateFine(int overdueDays) {
        if (overdueDays <= 0) {
            return 0;
        }
        return overdueDays * CD_FINE_RATE;
    }
    
    /**
     * Gets the fine rate per day for CDs.
     * 
     * @return the fine rate per day (20 NIS)
     */
    @Override
    public int getFineRatePerDay() {
        return CD_FINE_RATE;
    }
}

