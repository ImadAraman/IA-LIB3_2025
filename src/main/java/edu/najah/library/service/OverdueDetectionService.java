package edu.najah.library.service;

import edu.najah.library.domain.Loan;
import edu.najah.library.domain.LibraryItem;
import edu.najah.library.domain.User;
import edu.najah.library.domain.Fine;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for detecting overdue loans and calculating fines.
 * Identifies loans that exceed the 28-day borrowing period.
 * 
 * @author Imad Araman
 * @version 1.0
 */
public class OverdueDetectionService {
    
    private BorrowingService borrowingService;
    
    /**
     * Constructs an OverdueDetectionService with a reference to BorrowingService.
     * 
     * @param borrowingService the borrowing service to query loans
     */
    public OverdueDetectionService(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }
    
    /**
     * Gets all overdue loans as of a given date.
     * 
     * @param currentDate the date to check against
     * @return list of overdue loans
     */
    public List<Loan> getOverdueLoans(LocalDate currentDate) {
        if (borrowingService == null || currentDate == null) {
            return new ArrayList<>();
        }
        
        return borrowingService.getLoans().stream()
                .filter(loan -> loan.isOverdue(currentDate))
                .collect(Collectors.toList());
    }
    
    /**
     * Gets overdue loans for a specific user.
     * 
     * @param user the user
     * @param currentDate the date to check against
     * @return list of overdue loans for the user
     */
    public List<Loan> getOverdueLoansForUser(User user, LocalDate currentDate) {
        if (user == null || currentDate == null) {
            return new ArrayList<>();
        }
        
        return getOverdueLoans(currentDate).stream()
                .filter(loan -> user.equals(loan.getUser()))
                .collect(Collectors.toList());
    }
    
    /**
     * Calculates the fine amount for an overdue loan using the default strategy.
     * This method maintains backward compatibility with existing code.
     * 
     * <p>US5.2: Now automatically uses the correct fine strategy based on item type.</p>
     * 
     * @param loan the loan
     * @param currentDate the date to calculate fine as of
     * @return the fine amount, or 0 if loan is not overdue
     */
    public double calculateOverdueFine(Loan loan, LocalDate currentDate) {
        if (loan == null || currentDate == null) {
            return 0;
        }
        
        long daysOverdue = loan.getDaysOverdue(currentDate);
        
        if (daysOverdue <= 0) {
            return 0;
        }
        
        // US5.2: Use strategy-based calculation based on item type
        LibraryItem item = loan.getItem();
        if (item != null) {
            FineStrategyFactory.ItemType itemType = convertToFactoryType(item.getItemType());
            FineStrategy strategy = FineStrategyFactory.getStrategy(itemType);
            return calculateOverdueFine(loan, currentDate, strategy);
        }
        
        // Fallback to default (for backward compatibility)
        return Fine.calculateFineAmount(daysOverdue);
    }
    
    /**
     * Converts LibraryItem.ItemType to FineStrategyFactory.ItemType.
     * 
     * @param itemType the library item type
     * @return the factory item type
     */
    private FineStrategyFactory.ItemType convertToFactoryType(LibraryItem.ItemType itemType) {
        if (itemType == null) {
            return FineStrategyFactory.ItemType.BOOK; // Default
        }
        
        switch (itemType) {
            case BOOK:
                return FineStrategyFactory.ItemType.BOOK;
            case CD:
                return FineStrategyFactory.ItemType.CD;
            case JOURNAL:
                return FineStrategyFactory.ItemType.JOURNAL;
            default:
                return FineStrategyFactory.ItemType.BOOK;
        }
    }
    
    /**
     * Calculates the fine amount for an overdue loan using a specific fine strategy.
     * Uses the Strategy Design Pattern to allow different fine rates for different item types.
     * 
     * @param loan the loan
     * @param currentDate the date to calculate fine as of
     * @param strategy the fine calculation strategy to use
     * @return the fine amount in NIS, or 0 if loan is not overdue
     */
    public int calculateOverdueFine(Loan loan, LocalDate currentDate, FineStrategy strategy) {
        if (loan == null || currentDate == null || strategy == null) {
            return 0;
        }
        
        long daysOverdue = loan.getDaysOverdue(currentDate);
        
        if (daysOverdue <= 0) {
            return 0;
        }
        
        return strategy.calculateFine((int) daysOverdue);
    }
    
    /**
     * Calculates the fine amount for an overdue loan based on item type.
     * Automatically selects the appropriate strategy using FineStrategyFactory.
     * 
     * @param loan the loan
     * @param currentDate the date to calculate fine as of
     * @param itemType the type of library item (BOOK, CD, JOURNAL)
     * @return the fine amount in NIS, or 0 if loan is not overdue
     */
    public int calculateOverdueFine(Loan loan, LocalDate currentDate, FineStrategyFactory.ItemType itemType) {
        FineStrategy strategy = FineStrategyFactory.getStrategy(itemType);
        return calculateOverdueFine(loan, currentDate, strategy);
    }
    
    /**
     * Gets all overdue loans as of today.
     * 
     * @return list of overdue loans
     */
    public List<Loan> getOverdueLoans() {
        return getOverdueLoans(LocalDate.now());
    }
    
    /**
     * Gets overdue loans for a user as of today.
     * 
     * @param user the user
     * @return list of overdue loans for the user
     */
    public List<Loan> getOverdueLoansForUser(User user) {
        return getOverdueLoansForUser(user, LocalDate.now());
    }
    
    /**
     * Checks if a loan is overdue as of a given date.
     * 
     * @param loan the loan
     * @param currentDate the date to check against
     * @return true if the loan is overdue, false otherwise
     */
    public boolean isOverdue(Loan loan, LocalDate currentDate) {
        if (loan == null || currentDate == null) {
            return false;
        }
        
        return loan.isOverdue(currentDate);
    }
    
    /**
     * Checks if a loan is overdue as of today.
     * 
     * @param loan the loan
     * @return true if the loan is overdue, false otherwise
     */
    public boolean isOverdue(Loan loan) {
        return isOverdue(loan, LocalDate.now());
    }
    
    /**
     * Gets the number of days overdue for a loan.
     * 
     * @param loan the loan
     * @param currentDate the date to check against
     * @return number of days overdue, or 0 if not overdue
     */
    public long getDaysOverdue(Loan loan, LocalDate currentDate) {
        if (loan == null || currentDate == null) {
            return 0;
        }
        
        return loan.getDaysOverdue(currentDate);
    }
    
    /**
     * Gets the number of days overdue for a loan as of today.
     * 
     * @param loan the loan
     * @return number of days overdue, or 0 if not overdue
     */
    public long getDaysOverdue(Loan loan) {
        return getDaysOverdue(loan, LocalDate.now());
    }
    
    /**
     * Generates a mixed media overdue report for a user.
     * 
     * <p>US5.3: Includes both books and CDs (and other media types) in the fine summary.</p>
     * 
     * @param user the user
     * @param currentDate the date to check against
     * @return a map containing item type, count, and total fine for each type
     */
    public Map<String, Object> getMixedMediaOverdueReport(User user, LocalDate currentDate) {
        Map<String, Object> report = new HashMap<>();
        
        if (user == null || currentDate == null) {
            return report;
        }
        
        List<Loan> overdueLoans = getOverdueLoansForUser(user, currentDate);
        
        // Group by item type and calculate totals
        Map<LibraryItem.ItemType, List<Loan>> loansByType = overdueLoans.stream()
                .filter(loan -> loan.getItem() != null)
                .collect(Collectors.groupingBy(loan -> loan.getItem().getItemType()));
        
        int totalItems = overdueLoans.size();
        int totalFine = 0;
        
        Map<String, Object> byType = new HashMap<>();
        
        for (Map.Entry<LibraryItem.ItemType, List<Loan>> entry : loansByType.entrySet()) {
            LibraryItem.ItemType itemType = entry.getKey();
            List<Loan> typeLoans = entry.getValue();
            
            int typeCount = typeLoans.size();
            int typeFine = 0;
            
            FineStrategyFactory.ItemType factoryType = convertToFactoryType(itemType);
            FineStrategy strategy = FineStrategyFactory.getStrategy(factoryType);
            
            for (Loan loan : typeLoans) {
                long daysOverdue = loan.getDaysOverdue(currentDate);
                if (daysOverdue > 0) {
                    typeFine += strategy.calculateFine((int) daysOverdue);
                }
            }
            
            totalFine += typeFine;
            
            Map<String, Object> typeInfo = new HashMap<>();
            typeInfo.put("count", typeCount);
            typeInfo.put("totalFine", typeFine);
            byType.put(itemType.name(), typeInfo);
        }
        
        report.put("totalItems", totalItems);
        report.put("totalFine", totalFine);
        report.put("byType", byType);
        report.put("user", user.getUserId());
        report.put("reportDate", currentDate.toString());
        
        return report;
    }
    
    /**
     * Generates a mixed media overdue report for a user as of today.
     * 
     * <p>US5.3: Includes both books and CDs (and other media types) in the fine summary.</p>
     * 
     * @param user the user
     * @return a map containing item type, count, and total fine for each type
     */
    public Map<String, Object> getMixedMediaOverdueReport(User user) {
        return getMixedMediaOverdueReport(user, LocalDate.now());
    }
}
