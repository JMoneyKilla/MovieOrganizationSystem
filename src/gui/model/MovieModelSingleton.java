package gui.model;

public class MovieModelSingleton {
    private static MovieModelSingleton instance = null;
    private MovieModel movieModel = new MovieModel();

    private MovieModelSingleton(){}

    public static MovieModelSingleton getInstance(){
        if(instance == null){
            instance = new MovieModelSingleton();
        }
        return instance;
    }

    public MovieModel getMovieModel(){
        return movieModel;
    }
}