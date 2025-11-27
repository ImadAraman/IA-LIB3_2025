package edu.najah.library.service;

import edu.najah.library.domain.Book;
import edu.najah.library.domain.CD;
import edu.najah.library.domain.LibraryItem;
import edu.najah.library.domain.User;
import edu.najah.library.domain.Loan;
import edu.najah.library.domain.Fine;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing book borrowing, returns, and fine payments.
 * Handles loan creation, returns, and fine payment processing.
 * 
 * @author Imad Araman, Hamza Abuobaid
 * @version 1.0
 */
public class BorrowingService {
    
    private List<Loan> loans;
    private List<Fine> fines;
    
    /**
     * Default constructor initializing empty collections.
     */
    public BorrowingService() {
        this.loans = new ArrayList<>();
        this.fines = new ArrayList<>();
    }
    
    /**
     * Borrows a book for a user.
     * Book must be available and user must not have unpaid fines.
     * 
     * @param user the user borrowing the book
     * @param book the book to borrow
     * @return the created Loan, or null if borrow failed
     */
    public Loan borrowBook(User user, Book book) {
        return borrowBook(user, book, LocalDate.now());
    }
    
    /**
     * Borrows a book for a user on a specific date.
     * Book must be available and user must not have unpaid fines or overdue books.
     * 
     * <p>US4.1: Users cannot borrow if they have overdue books or unpaid fines.</p>
     * 
     * @param user the user borrowing the book
     * @param book the book to borrow
     * @param borrowDate the date of borrowing
     * @return the created Loan, or null if borrow failed
     * @throws IllegalStateException if user has overdue books or unpaid fines
     */
    public Loan borrowBook(User user, Book book, LocalDate borrowDate) {
        if (user == null || book == null || borrowDate == null) {
            return null;
        }
        
        // Check if book is available
        if (!book.isAvailable()) {
            return null;
        }
        
        // Check if user can borrow (no unpaid fines and no overdue books)
        if (!canBorrow(user, borrowDate)) {
            // Get the specific reason
            if (hasUnpaidFines(user)) {
                throw new IllegalStateException("Cannot borrow books: user has unpaid fines.");
            }
            if (hasOverdueBooks(user, borrowDate)) {
                throw new IllegalStateException("Cannot borrow books: user has overdue books.");
            }
            return null;
        }
        
        // Create loan
        Loan loan = new Loan(book, user, borrowDate);
        
        // Mark book as unavailable
        book.setAvailable(false);
        
        // Add to loans list
        loans.add(loan);
        
        return loan;
    }
    
    /**
     * Borrows a CD for a user.
     * CD must be available and user must not have unpaid fines or overdue items.
     * 
     * <p>US5.1: CDs are borrowed for 7 days (different from books which are 28 days).</p>
     * 
     * @param user the user borrowing the CD
     * @param cd the CD to borrow
     * @return the created Loan, or null if borrow failed
     * @throws IllegalStateException if user has overdue items or unpaid fines
     */
    public Loan borrowCD(User user, CD cd) {
        return borrowCD(user, cd, LocalDate.now());
    }
    
    /**
     * Borrows a CD for a user on a specific date.
     * CD must be available and user must not have unpaid fines or overdue items.
     * 
     * <p>US5.1: CDs are borrowed for 7 days (different from books which are 28 days).</p>
     * 
     * @param user the user borrowing the CD
     * @param cd the CD to borrow
     * @param borrowDate the date of borrowing
     * @return the created Loan, or null if borrow failed
     * @throws IllegalStateException if user has overdue items or unpaid fines
     */
    public Loan borrowCD(User user, CD cd, LocalDate borrowDate) {
        if (user == null || cd == null || borrowDate == null) {
            return null;
        }
        
        // Check if CD is available
        if (!cd.isAvailable()) {
            return null;
        }
        
        // Check if user can borrow (no unpaid fines and no overdue items)
        if (!canBorrow(user, borrowDate)) {
            // Get the specific reason
            if (hasUnpaidFines(user)) {
                throw new IllegalStateException("Cannot borrow CDs: user has unpaid fines.");
            }
            if (hasOverdueBooks(user, borrowDate)) {
                throw new IllegalStateException("Cannot borrow CDs: user has overdue items.");
            }
            return null;
        }
        
        // Create loan (CDs use 7-day loan period automatically via LibraryItem interface)
        Loan loan = new Loan(cd, user, borrowDate);
        
        // Mark CD as unavailable
        cd.setAvailable(false);
        
        // Add to loans list
        loans.add(loan);
        
        return loan;
    }
    
    /**
     * Borrows any library item (polymorphic method).
     * 
     * <p>US5.1: Automatically uses correct loan period based on item type.</p>
     * 
     * @param user the user borrowing the item
     * @param item the library item to borrow
     * @param borrowDate the date of borrowing
     * @return the created Loan, or null if borrow failed
     * @throws IllegalStateException if user has overdue items or unpaid fines
     */
    public Loan borrowItem(User user, LibraryItem item, LocalDate borrowDate) {
        if (item == null) {
            return null;
        }
        
        if (item instanceof Book) {
            return borrowBook(user, (Book) item, borrowDate);
        } else if (item instanceof CD) {
            return borrowCD(user, (CD) item, borrowDate);
        }
        
        return null;
    }
    
    /**
     * Returns a borrowed item.
     * 
     * @param loan the loan to process return for
     * @param returnDate the return date
     */
    public void returnItem(Loan loan, LocalDate returnDate) {
        if (loan == null || returnDate == null) {
            return;
        }
        
        loan.setReturnDate(returnDate);
        
        // Mark item as available (polymorphic handling)
        LibraryItem item = loan.getItem();
        if (item != null) {
            item.setAvailable(true);
        } else if (loan.getBook() != null) {
            // Backward compatibility
            loan.getBook().setAvailable(true);
        }
    }
    
    /**
     * Returns a borrowed book.
     * 
     * @param loan the loan to process return for
     * @param returnDate the return date
     */
    public void returnBook(Loan loan, LocalDate returnDate) {
        returnItem(loan, returnDate);
    }
    
    /**
     * Returns a borrowed book with current date.
     * 
     * @param loan the loan to process return for
     */
    public void returnBook(Loan loan) {
        returnBook(loan, LocalDate.now());
    }
    
    /**
     * Gets all active loans for a user.
     * 
     * @param user the user
     * @return list of active loans (not returned)
     */
    public List<Loan> getActiveLoans(User user) {
        if (user == null) {
            return new ArrayList<>();
        }
        
        return loans.stream()
                .filter(loan -> user.equals(loan.getUser()) && !loan.isReturned())
                .collect(Collectors.toList());
    }
    
    /**
     * Gets all loans for a user (active and returned).
     * 
     * @param user the user
     * @return list of all loans for the user
     */
    public List<Loan> getAllLoans(User user) {
        if (user == null) {
            return new ArrayList<>();
        }
        
        return loans.stream()
                .filter(loan -> user.equals(loan.getUser()))
                .collect(Collectors.toList());
    }
    
    /**
     * Pays a fine for a user.
     * 
     * @param user the user paying the fine
     * @param amount the payment amount
     * @return true if payment was successful, false otherwise
     */
    public boolean payFine(User user, double amount) {
        if (user == null || amount <= 0) {
            return false;
        }
        
        // Find unpaid fines for user
        List<Fine> userFines = fines.stream()
                .filter(fine -> user.equals(fine.getUser()) && !fine.isPaid())
                .collect(Collectors.toList());
        
        if (userFines.isEmpty()) {
            return false;
        }
        
        double remainingPayment = amount;
        
        // Pay fines in order
        for (Fine fine : userFines) {
            if (remainingPayment <= 0) break;
            
            if (remainingPayment >= fine.getAmount()) {
                remainingPayment -= fine.getAmount();
                fine.payFine(fine.getAmount());
            } else {
                fine.payFine(remainingPayment);
                remainingPayment = 0;
            }
        }
        
        return true;
    }
    
    /**
     * Checks if a user can borrow (has no unpaid fines and no overdue books).
     * 
     * <p>US4.1: Users cannot borrow if they have overdue books or unpaid fines.</p>
     * 
     * @param user the user
     * @return true if user can borrow, false if has unpaid fines or overdue books
     */
    public boolean canBorrow(User user) {
        return canBorrow(user, LocalDate.now());
    }
    
    /**
     * Checks if a user can borrow as of a specific date.
     * 
     * <p>US4.1: Users cannot borrow if they have overdue books or unpaid fines.</p>
     * 
     * @param user the user
     * @param currentDate the date to check against
     * @return true if user can borrow, false if has unpaid fines or overdue books
     */
    public boolean canBorrow(User user, LocalDate currentDate) {
        if (user == null || currentDate == null) {
            return false;
        }
        
        // Check for unpaid fines
        if (hasUnpaidFines(user)) {
            return false;
        }
        
        // Check for overdue books
        if (hasOverdueBooks(user, currentDate)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Checks if a user has any overdue books as of a specific date.
     * 
     * @param user the user
     * @param currentDate the date to check against
     * @return true if user has overdue books, false otherwise
     */
    public boolean hasOverdueBooks(User user, LocalDate currentDate) {
        if (user == null || currentDate == null) {
            return false;
        }
        
        return getActiveLoans(user).stream()
                .anyMatch(loan -> loan.isOverdue(currentDate));
    }
    
    /**
     * Checks if a user has unpaid fines.
     * 
     * @param user the user
     * @return true if user has unpaid fines, false otherwise
     */
    public boolean hasUnpaidFines(User user) {
        if (user == null) {
            return false;
        }
        
        return fines.stream()
                .anyMatch(fine -> user.equals(fine.getUser()) && !fine.isPaid());
    }
    
    /**
     * Gets all unpaid fines for a user.
     * 
     * @param user the user
     * @return list of unpaid fines
     */
    public List<Fine> getUnpaidFines(User user) {
        if (user == null) {
            return new ArrayList<>();
        }
        
        return fines.stream()
                .filter(fine -> user.equals(fine.getUser()) && !fine.isPaid())
                .collect(Collectors.toList());
    }
    
    /**
     * Adds a fine to the system.
     * 
     * @param fine the fine to add
     */
    public void addFine(Fine fine) {
        if (fine != null) {
            fines.add(fine);
        }
    }
    
    /**
     * Gets all loans.
     * 
     * @return list of all loans
     */
    public List<Loan> getLoans() {
        return new ArrayList<>(loans);
    }
    
    /**
     * Gets all fines.
     * 
     * @return list of all fines
     */
    public List<Fine> getFines() {
        return new ArrayList<>(fines);
    }
}
