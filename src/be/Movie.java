package be;

import javafx.beans.property.*;

public class Movie {
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty name = new SimpleStringProperty();
    private StringProperty rating = new SimpleStringProperty();
    private StringProperty absolutePath = new SimpleStringProperty();
    private StringProperty lastViewed = new SimpleStringProperty();
    private StringProperty imdbRating = new SimpleStringProperty();

    public Movie (int id, String name, String rating, String absolutePath, String lastViewed, String imdbRating) {
        setId(id);
        setName(name);
        setRating(rating);
        setAbsolutePath(absolutePath);
        setLastViewed(lastViewed);
        setImdbRating(imdbRating);
    }

    public Movie(String name, String absolutePath) {
        setName(name);
        setAbsolutePath(absolutePath);
    }

    public Movie(){

    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getRating() {
        return rating.get();
    }

    public String getImdbRating() {
        return imdbRating.get();
    }

    public StringProperty imdbRatingProperty()
    {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating.set(imdbRating);
    }


    public StringProperty ratingProperty() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating.set(rating);
    }

    public String getAbsolutePath() {
        return absolutePath.get();
    }

    public StringProperty absolutePathProperty() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath.set(absolutePath);
    }

    public String getLastViewed() {
        return lastViewed.get();
    }

    public StringProperty lastViewedProperty() {
        return lastViewed;
    }

    public void setLastViewed(String lastViewed) {
        this.lastViewed.set(lastViewed);
    }

    @Override
    public String toString() {
        return id +" "+ name +" "+ rating + " " + absolutePath +" "+ lastViewed+" "+imdbRating;
    }
}
