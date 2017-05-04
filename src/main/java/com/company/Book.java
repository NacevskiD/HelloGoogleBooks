package com.company;

/**
 * Created by clara on 5/3/17.
 */
public class Book {
    String title;
    String author;
    String isbn;
    String description;
    double googleRating;
    double myRating;

    public Book(String title, String author, String description, String isbn, double googleRating) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.description = description;
        this.googleRating = googleRating;
    }

}
