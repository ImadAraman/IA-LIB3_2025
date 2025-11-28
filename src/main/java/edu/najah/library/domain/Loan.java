package edu.najah.library.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a loan/borrowing record for any library item.
 * Tracks when an item was borrowed, when it's due, and when it was returned.
 * 
 * <p>Supports polymorphism to handle different item types (books, CDs, journals)
 * with different loan periods.</p>
 * 
 * <p>US5.1: CDs have a 7-day loan period, books have 28 days.</p>
 * 
 * @author Imad Araman
 * @version 1.0
 */
@Entity
@Table(name = "loans")
public class Loan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    // Store item type and identifier for polymorphic handling
    @Column(name = "item_type", nullable = false, length = 20)
    private String itemType;
    
    @Column(name = "item_identifier", nullable = false, length = 100)
    private String itemIdentifier;
    
    @Column(name = "item_title", nullable = false, length = 255)
    private String itemTitle;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate;
    
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;
    
    @Column(name = "return_date")
    private LocalDate returnDate;
    
    // Transient field for the actual item (not persisted, loaded separately)
    @Transient
    private LibraryItem item;
    
    /**
     * Default constructor required by JPA.
     */
    public Loan() {
    }
    
    /**
     * Constructs a Loan with a library item, user, and borrow date.
     * Due date is calculated based on the item's loan period.
     * 
     * <p>US5.1: Automatically uses correct loan period (7 days for CDs, 28 days for books).</p>
     * 
     * @param item the borrowed library item (Book, CD, etc.)
     * @param user the user borrowing the item
     * @param borrowDate the date of borrowing
     */
    public Loan(LibraryItem item, User user, LocalDate borrowDate) {
        if (item == null || user == null || borrowDate == null) {
            throw new IllegalArgumentException("Item, user, and borrowDate cannot be null");
        }
        this.item = item;
        this.itemType = item.getItemType().name();
        this.itemIdentifier = item.getUniqueIdentifier();
        this.itemTitle = item.getTitle();
        this.user = user;
        this.borrowDate = borrowDate;
        this.dueDate = borrowDate.plusDays(item.getLoanPeriodDays());
        this.returnDate = null;
    }
    
    /**
     * Legacy constructor for Book (maintains backward compatibility).
     * 
     * @param book the borrowed book
     * @param user the user borrowing the book
     * @param borrowDate the date of borrowing
     */
    public Loan(Book book, User user, LocalDate borrowDate) {
        this((LibraryItem) book, user, borrowDate);
    }
    
    /**
     * Gets the database ID.
     * 
     * @return the loan ID
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Sets the database ID.
     * 
     * @param id the ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Gets the borrowed item.
     * 
     * @return the library item
     */
    public LibraryItem getItem() {
        return item;
    }
    
    /**
     * Sets the borrowed item.
     * 
     * @param item the item to set
     */
    public void setItem(LibraryItem item) {
        this.item = item;
        if (item != null) {
            this.itemType = item.getItemType().name();
            this.itemIdentifier = item.getUniqueIdentifier();
            this.itemTitle = item.getTitle();
        }
    }
    
    /**
     * Gets the borrowed book (for backward compatibility).
     * 
     * @return the book if item is a Book, null otherwise
     */
    public Book getBook() {
        if (item instanceof Book) {
            return (Book) item;
        }
        return null;
    }
    
    /**
     * Sets the borrowed book (for backward compatibility).
     * 
     * @param book the book to set
     */
    public void setBook(Book book) {
        setItem(book);
    }
    
    /**
     * Gets the borrowed CD.
     * 
     * @return the CD if item is a CD, null otherwise
     */
    public CD getCD() {
        if (item instanceof CD) {
            return (CD) item;
        }
        return null;
    }
    
    /**
     * Gets the item type.
     * 
     * @return the item type
     */
    public String getItemType() {
        return itemType;
    }
    
    /**
     * Gets the item identifier.
     * 
     * @return the item identifier
     */
    public String getItemIdentifier() {
        return itemIdentifier;
    }
    
    /**
     * Gets the item title.
     * 
     * @return the item title
     */
    public String getItemTitle() {
        return itemTitle;
    }
    
    /**
     * Gets the user.
     * 
     * @return the user
     */
    public User getUser() {
        return user;
    }
    
    /**
     * Sets the user.
     * 
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }
    
    /**
     * Gets the borrow date.
     * 
     * @return the borrow date
     */
    public LocalDate getBorrowDate() {
        return borrowDate;
    }
    
    /**
     * Sets the borrow date.
     * Recalculates due date based on item's loan period if item is set.
     * 
     * @param borrowDate the borrow date to set
     */
    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
        if (this.dueDate == null && item != null) {
            this.dueDate = borrowDate.plusDays(item.getLoanPeriodDays());
        }
    }
    
    /**
     * Gets the due date.
     * 
     * @return the due date
     */
    public LocalDate getDueDate() {
        return dueDate;
    }
    
    /**
     * Sets the due date.
     * 
     * @param dueDate the due date to set
     */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    
    /**
     * Gets the return date.
     * 
     * @return the return date, or null if not yet returned
     */
    public LocalDate getReturnDate() {
        return returnDate;
    }
    
    /**
     * Sets the return date.
     * 
     * @param returnDate the return date to set
     */
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    
    /**
     * Checks if this loan is overdue as of a given date.
     * A loan is overdue if current date is after due date and book hasn't been returned.
     * 
     * @param currentDate the date to check against
     * @return true if overdue, false otherwise
     */
    public boolean isOverdue(LocalDate currentDate) {
        return returnDate == null && currentDate.isAfter(dueDate);
    }
    
    /**
     * Calculates the number of days overdue as of a given date.
     * 
     * @param currentDate the date to check against
     * @return number of days overdue, or 0 if not overdue
     */
    public long getDaysOverdue(LocalDate currentDate) {
        if (!isOverdue(currentDate)) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(dueDate, currentDate);
    }
    
    /**
     * Checks if the book has been returned.
     * 
     * @return true if returned, false otherwise
     */
    public boolean isReturned() {
        return returnDate != null;
    }
    
    /**
     * Compares this loan with another object for equality.
     * 
     * @param obj the object to compare with
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Loan loan = (Loan) obj;
        return Objects.equals(itemIdentifier, loan.itemIdentifier) &&
               Objects.equals(user, loan.user) &&
               Objects.equals(borrowDate, loan.borrowDate);
    }
    
    /**
     * Generates a hash code for this loan.
     * 
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(itemIdentifier, user, borrowDate);
    }
    
    /**
     * Returns a string representation of this loan.
     * 
     * @return string representation
     */
    @Override
    public String toString() {
        return "Loan{" +
                "item=" + (item != null ? item.getTitle() : itemTitle) +
                ", itemType=" + itemType +
                ", user=" + user +
                ", borrowDate=" + borrowDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                '}';
    }
}
