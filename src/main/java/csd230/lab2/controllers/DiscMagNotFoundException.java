package csd230.lab2.controllers;

public class DiscMagNotFoundException extends RuntimeException {
    public DiscMagNotFoundException(long id) {
        super("Could not find disc magazine " + id);
    }
}
