package csd230.lab2.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.util.Objects;

@Entity
public class Book extends Publication {

    @Column(name = "author")
    private String author;

    @Column(name = "ISBN")
    private String ISBN;

    public Book() {}

    public Book(double price, int quantity, String description, String title, int copies, String author, String isbn) {
        super(price, quantity, description, title, copies);
        this.author = author;
        this.ISBN = isbn;
    }

    // Getters and Setters

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return ISBN;
    }

    public void setIsbn(String isbn) {
        this.ISBN = isbn;
    }

    @Override
    public String toString() {
        return "Book{" +
                "author='" + author + '\'' +
                ", ISBN='" + ISBN + '\'' +
                ", " + super.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        if (!super.equals(o)) return false;
        Book book = (Book) o;
        return Objects.equals(author, book.author) &&
                Objects.equals(ISBN, book.ISBN);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), author, ISBN);
    }
}
