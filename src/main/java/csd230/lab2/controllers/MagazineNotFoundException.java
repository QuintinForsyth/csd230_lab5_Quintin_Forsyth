package csd230.lab2.controllers;

public class MagazineNotFoundException extends RuntimeException {
  public MagazineNotFoundException(long id) {
    super("Could not find magazine " + id);
  }
}
