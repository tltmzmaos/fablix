package com.example.fablixandroid;

public class Movie {
    private String id;
    private String name;
    private String year;
    private String director;
    private String[] genre;
    private String[] star;

    public Movie(String id, String name, String year, String director, String[] genre, String[] star) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.director = director;
        this.genre = genre;
        this.star = star;
    }

    public String getId(){ return id; }
    public String getName() {
        return name;
    }
    public String getYear() {
        return year;
    }
    public String getDirector(){ return director; }
    public String[] getGenre(){ return genre; }
    public String[] getStar(){ return star; }
}
