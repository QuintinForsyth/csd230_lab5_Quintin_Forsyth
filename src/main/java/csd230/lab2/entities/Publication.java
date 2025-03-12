package csd230.lab2.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "publication_type")
public class Publication extends CartItem {

    @Column(name = "title")
    private String title;

    @Column(name = "copies", nullable = true)
    private int copies;

    public Publication() {}

    public Publication(double price, int quantity, String description, String title, int copies) {
        super(price, quantity, description);
        this.title = title;
        this.copies = copies;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    @Override
    public String toString() {
        return "Publication{" +
                "title='" + title + '\'' +
                ", copies=" + copies +
                ", " + super.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Publication)) return false;
        if (!super.equals(o)) return false;
        Publication that = (Publication) o;
        return copies == that.copies &&
                Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, copies);
    }
}
