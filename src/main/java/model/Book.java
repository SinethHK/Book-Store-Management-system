/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Sineth HK
 */


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class Book {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("authorId")
    private Long authorId;
    @JsonProperty("isbn")
    private String isbn;
    @JsonProperty("publicationYear")
    private int publicationYear;
    @JsonProperty("price")
    private double price;
    @JsonProperty("stock")
    private int stock;

    public Book() {}

    public Book(Long id, String title, Long authorId, String isbn, int publicationYear, double price, int stock) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.price = price;
        this.stock = stock;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public int getPublicationYear() { return publicationYear; }
    public void setPublicationYear(int publicationYear) { this.publicationYear = publicationYear; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return publicationYear == book.publicationYear &&
               Double.compare(book.price, price) == 0 &&
               stock == book.stock &&
               Objects.equals(id, book.id) &&
               Objects.equals(title, book.title) &&
               Objects.equals(authorId, book.authorId) &&
               Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, authorId, isbn, publicationYear, price, stock);
    }
}