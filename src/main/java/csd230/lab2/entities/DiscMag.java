package csd230.lab2.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.util.Objects;

@Entity
public class DiscMag extends Magazine {
    @Column(name = "has_disc", nullable = true)
    private boolean hasDisc;

    public boolean getHasDisc() {
        return hasDisc;
    }

    public void setHasDisc(boolean hasDisc) {
        this.hasDisc = hasDisc;
    }

    public DiscMag() {
        hasDisc = false;
    }

    public DiscMag(boolean hasDisc) {
        this.hasDisc = hasDisc;
    }

    @Override
    public String toString() {
        return "DiscMag{" +
                "hasDisc=" + hasDisc +
                ", " + super.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiscMag)) return false;
        if (!super.equals(o)) return false;
        DiscMag discMag = (DiscMag) o;
        return hasDisc == discMag.hasDisc;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), hasDisc);
    }
}
