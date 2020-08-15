package com.example.boone.app3;

public class Book {

    private int id;

    private int isbn;

    private String title;

    private int edition;

    private String author;

    private String classification;

    private String language;

    private String age_group;

    private String genre;

    public Book(int num) {
        this.isbn = 12345;
        this.title = "Title #" + num;
        this.edition = 0;
        this.author = "Author #" + num;
        this.classification = "Classification #" + num;
        this.language = "Language #" + num;
        this.age_group = "20";
        this.genre = "Genre #" + num;
    }

    public Book(int isbn, String title, int edition, String author, String classification, String language, String age_group, String genre) {
        this.isbn = isbn;
        this.title = title;
        this.edition = edition;
        this.author = author;
        this.classification = classification;
        this.language = language;
        this.age_group = age_group;
        this.genre = genre;
    }

    public int getId() {
        return id;
    }

    public int getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public int getEdition() {
        return edition;
    }

    public String getAuthor() {
        return author;
    }

    public String getClassification() {
        return classification;
    }

    public String getLanguage() {
        return language;
    }

    public String getAge_group() {
        return age_group;
    }

    public String getGenre() {
        return genre;
    }
}

