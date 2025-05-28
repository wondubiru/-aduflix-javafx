package media;

import java.util.Objects;

public abstract class MediaItems implements Comparable<MediaItems>, Playable{
    private String title, genre;
    private double rating;
    private double duration;
    private static int totalWatched = 0;
    public MediaItems(String title, String genre, double rating, double duration){
        this(title, genre);

        this.rating = rating;
    }
    MediaItems(String title, String genre){
        this.title = title;
        this.genre = genre;
        this.duration = 0.0;
        rating = 0.0; // default rating
    }


    public String  getTitle() {return title;}

    public String getGenre(){return genre;}

    public double getRating(){return rating;}
    public double getDuration(){
        return duration;}

    public static int getTotalWatched() {
        return totalWatched;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public static void incrementWatched() {
        totalWatched++;
    }

    public void setRating(double rating){
        if(rating >= 0 && rating <= 10){
            this.rating = rating;}
        else {
            System.out.println("rating can only be within the range(0-10) ");
        }
    }
    public void setDuration(double duration){
        if(duration > 0){
            this.duration = duration;
        }
        else {
            System.out.println("Duration Must be greater than 0! ");
        }
    }



    @Override
    public int compareTo(MediaItems other){

        return Double.compare(this.rating, rating);

    }
    @Override
    public String toString(){
        return String.format("%s (%s) - rating: %.1f, Duration: %.2f hr: ", title, genre, rating, duration);
    }

    @Override

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MediaItems)) return false;
        MediaItems other = (MediaItems) obj;
        return Objects.equals(this.title, other.title)
                && Objects.equals(this.genre, other.genre)
                && this.duration == other.duration;
    }


    public abstract String getDetails();





}
