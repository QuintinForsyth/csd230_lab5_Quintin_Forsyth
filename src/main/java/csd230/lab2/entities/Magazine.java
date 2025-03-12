package csd230.lab2.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.util.Date;
import java.util.Objects;

@Entity
public class Magazine extends Publication {
    @Column(name = "order_qty", nullable = true)
    private int orderQty;

    @Column(name = "curr_issue")
    private Date currIssue;

    // Getters and setters
    public int getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
    }

    public Date getCurrIssue() {
        return currIssue;
    }

    public void setCurrIssue(Date currIssue) {
        this.currIssue = currIssue;
    }

    // Constructors
    public Magazine() {
    }

    public Magazine(double price, int quantity, String description, String title, int copies, int orderQty, Date currIssue) {
        super(price, quantity, description, title, copies);
        this.orderQty = orderQty;
        this.currIssue = currIssue;
    }

    @Override
    public String toString() {
        return "Magazine{" +
                "orderQty=" + orderQty +
                ", currIssue=" + currIssue +
                ", " + super.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Magazine)) return false;
        if (!super.equals(o)) return false;
        Magazine magazine = (Magazine) o;
        return orderQty == magazine.orderQty &&
                Objects.equals(currIssue, magazine.currIssue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), orderQty, currIssue);
    }
}
