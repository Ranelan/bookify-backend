package com.booklify.dto;
import com.booklify.domain.Book;
import com.booklify.domain.enums.BookCondition;

import java.time.LocalDateTime;
import java.util.Arrays;

public class BookDto {

    private Long bookID;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private BookCondition condition;
    private Double price;
    private String description;
    private LocalDateTime uploadedDate;
    private byte[] image;
    private Long userId;
    private String uploaderName;
    private String uploaderEmail;

    public BookDto() {
    }

    public BookDto(Long bookID, String isbn, String title, String author, String publisher,
                   BookCondition condition, Double price, String description,
                   LocalDateTime uploadedDate, byte[] image) {
        this.bookID = bookID;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.condition = condition;
        this.price = price;
        this.description = description;
        this.uploadedDate = uploadedDate;
        this.image = image;
    }

    public static BookDto fromEntity(Book book) {
        BookDto dto = new BookDto(
                book.getBookID(),
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getCondition(),
                book.getPrice(),
                book.getDescription(),
                book.getUploadedDate(),
                book.getImage()
        );
        if (book.getUser() != null) {
            dto.setUserId(book.getUser().getId());
            dto.setUploaderName(book.getUser().getFullName());
            dto.setUploaderEmail(book.getUser().getEmail());
        }
        return dto;
    }


    public static Book toEntity(BookDto dto) {
        return new Book.Builder()
                .setBookID(dto.getBookID())
                .setIsbn(dto.getIsbn())
                .setTitle(dto.getTitle())
                .setAuthor(dto.getAuthor())
                .setPublisher(dto.getPublisher())
                .setCondition(dto.getCondition())
                .setPrice(dto.getPrice())
                .setDescription(dto.getDescription())
                .setUploadedDate(dto.getUploadedDate())
                .setImage(dto.getImage())
                .build();
    }

    // Getters and Setters

    public Long getBookID() {
        return bookID;
    }

    public void setBookID(Long bookID) {
        this.bookID = bookID;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public BookCondition getCondition() {
        return condition;
    }

    public void setCondition(BookCondition condition) {
        this.condition = condition;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(LocalDateTime uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUploaderName() {
        return uploaderName;
    }
    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }
    public String getUploaderEmail() {
        return uploaderEmail;
    }
    public void setUploaderEmail(String uploaderEmail) {
        this.uploaderEmail = uploaderEmail;
    }

    @Override
    public String toString() {
        return "BookDto{" +
                "bookID=" + bookID +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", condition=" + condition +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", uploadedDate=" + uploadedDate +
                ", UserId =" + userId +
                ", uploaderName='" + uploaderName + '\'' +
                ", uploaderEmail='" + uploaderEmail + '\'' +
                '}';
    }

}


