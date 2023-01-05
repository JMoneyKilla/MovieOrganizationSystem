package be;

import javafx.beans.property.*;

public class Movie {
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty name = new SimpleStringProperty();
    private DoubleProperty rating = new SimpleDoubleProperty();
    private StringProperty absolutePath = new SimpleStringProperty();
    private StringProperty lastViewed = new SimpleStringProperty();

    public Movie (int id, String name, double rating, String absolutePath, String lastViewed) {
        setId(id);
        setName(name);
        setRating(rating);
        setAbsolutePath(absolutePath);
        setLastViewed(lastViewed);
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

    public double getRating() {
        return rating.get();
    }

    public DoubleProperty ratingProperty() {
        return rating;
    }

    public void setRating(double rating) {
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
        return id +" "+ name +" "+ rating + " " + absolutePath +" "+ lastViewed;
    }
}
