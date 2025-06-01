package media;

import java.util.Objects;

public abstract class MediaItems implements Comparable<MediaItems>, Playable{
    private String title, genre;
    private double rating;
    private double duration;
    private static int totalWatched = 0;

    /**
     * Constructor for MediaItems class.
     * Initializes the title, genre, rating, and duration of the media item.
     *
     * @param title    The title of the media item.
     * @param genre    The genre of the media item.
     * @param rating   The rating of the media item (0.0 to 10.0).
     * @param duration The duration of the media item in hours.
     */
    public MediaItems(String title, String genre, double rating, double duration){
        this.rating = rating;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
    }

    public MediaItems(String title, String genre){
       this(title, genre, 0.0, 2);
    }

    /**
     * Gets the title of the media item.
     *
     * @return The title of the media item.
     */

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
        if(rating > 0 && rating < 10){
            this.rating = rating;}
        else {
            System.out.println("rating can only be within the range(0-10) ");
        }
    }

    public static MediaItems parse(String line) {
        try {
            String[] parts = line.split(",");

            switch (parts[0].toLowerCase()) {
                case "movie":
                    return new Movie(
                            parts[1],                             // title
                            parts[2],                             // genre
                            Double.parseDouble(parts[3]),         // rating
                            Double.parseDouble(parts[4]),         // duration
                            parts[5]                              // director
                    );
                case "series":
                    return new Series(
                            parts[1],
                            parts[2],
                            Double.parseDouble(parts[3]),
                            Double.parseDouble(parts[4]),
                            Integer.parseInt(parts[5]),
                            Integer.parseInt(parts[6])
                    );
                case "documentary":
                    return new Documentary(
                            parts[1],
                            parts[2],
                            Double.parseDouble(parts[3]),
                            Double.parseDouble(parts[4]),
                            parts[5]
                    );
            }
        } catch (Exception e) {
            System.out.println(" Error parsing line: " + line);
        }
        return null;
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

        return Double.compare(this.rating, other.rating);
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
