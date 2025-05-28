package media;

import media.MediaItems;
import media.Playable;
import media.Rateable;

public class Series extends MediaItems implements Playable, Rateable {

    private int numberOfEpisodes;
    private int seasons;

    public Series(String title, String genre, double rating, double duration, int numberOfEpisodes, int seasons){
        super(title, genre, rating, duration);
        this.numberOfEpisodes = numberOfEpisodes;
        this.seasons = seasons;
    }
// overloaded constructor
    public Series(String title, String genre){
        this(title, genre, 0.0, 0, 0, 0);
    }

    public int getSeasons() {
        return seasons;
    }

    public void setSeasons(int seasons) {
        this.seasons = seasons;
    }

    @Override
    public void rate(double rating) {
        setRating(rating);
        System.out.println("You rated the series '" + getTitle() + "' a " + rating + " out of 10.");
    }

    @Override
    public String getDetails() {
        return "";
    }

    public int getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public void setNumberOfEpisodes(int numberOfEpisodes) {
        this.numberOfEpisodes = numberOfEpisodes;
    }

    @Override
    public void play() {
        System.out.println("Streaming series: " + getTitle() + " [" + seasons + " seasons]");
    }

}