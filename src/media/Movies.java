package media;

import media.MediaItems;

import java.util.ArrayList;

public class Movies extends MediaItems implements Playable, Savable, Rateable{
    private String director;

    public Movies(String title, String genre, double rating, double duration, String directory){
        super(title, genre, rating, duration);
        this.director = directory;
    }
    public Movies(String title, String genre, String director){
        super(title, genre);
        this.director = director;
    }
    public void play(){
        System.out.println("playing the Movie: " + super.getTitle());}


    @Override
    public String getDetails() {
        return "🎬 Movie: " + getTitle() +
                "\nGenre: " + getGenre() +
                "\nDirector: " + director +
                "\nRating: " + getRating() +
                "\nDuration: " + getDuration() + " minutes";
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    @Override

    public void rate(double rating){
        System.out.println("You rated " + getTitle() + " a " + rating + " out of ten ");
    }
}