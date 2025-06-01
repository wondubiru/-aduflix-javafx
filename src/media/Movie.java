package media;

public class Movie extends MediaItems implements Playable, Savable, Rateable{
    private String director;

    public Movie(String title, String genre, double rating, double duration, String directory){
        super(title, genre, rating, duration);
        this.director = directory;
    }
    public Movie(String title, String genre, String director){
        this(title, genre, 0.0, 0.0, director);
    }
    public void play(){
        System.out.println("playing the Movie: " + getTitle());}


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