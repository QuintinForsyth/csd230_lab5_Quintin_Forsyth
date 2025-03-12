package csd230.lab2.controllers;

public class BookNotFoundException extends RuntimeException {
  public BookNotFoundException(long id) {
    super("Could not find book " +  id);
  }
}
