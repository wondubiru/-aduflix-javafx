package media;

public class Documentary extends MediaItems implements Playable, Rateable{
    private String subject;
    public Documentary(String title, String genre, double rating, double duration, String subje){
        super(title, genre, rating, duration);
        this.subject = subject;
    }

    public Documentary(String title, String genre, String subject) {
      this(title, genre, 0.0, 0, subject);
    }

    @Override
    public String getDetails() {
        return " Documentary: " + getTitle() +
                "\nGenre: " + getGenre() +
                "\nNarrated by: " + subject +
                "\nRating: " + getRating() +
                "\nDuration: " + getDuration() + " minutes";
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public void rate(double rating) {
        setRating(rating);
        System.out.println("You rated the documentary '" + getTitle() + "' a " + rating + " out of 10.");
    }

    public void play(){
        System.out.println("plating Series " + getTitle());
    }

}