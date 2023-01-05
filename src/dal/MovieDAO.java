package dal;

import be.Movie;
import be.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {
    private PreparedStatement preparedStatement;
    DBConnection dbConnection = new DBConnection();


    /**
     * retrieves all movies stored in db
     * @return list of all movies in db
     * @throws SQLException
     */
    public List<Movie> getAllMovies() throws SQLException {
        Movie movie;
        List<Movie> retrievedMovies = new ArrayList<>();
        try(Connection connection = dbConnection.getConnection()){
            String sql ="SELECT id, name, rating, absolute_path, last_viewed FROM Movie";
            preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Double rating = rs.getDouble("rating");
                String absolutePath = rs.getString("absolute_path");
                String lastViewed = rs.getString("last_viewed");
                movie = new Movie(id, name, rating, absolutePath, lastViewed);
                retrievedMovies.add(movie);
            }
        }
        return retrievedMovies;
    }

    /**
     * finds movie from db based on given id
     * @param id
     * @return movie based off given id
     * @throws SQLException
     */
    public Movie getMovieByID(int id) throws SQLException {
        List<Movie> movies = getAllMovies();
        for (Movie m: movies
        ) {
            if(m.getId() == id)
                return m;
        }
        return null;
    }

    /**
     * finds and deletes movie from db based on given id
     * @param id
     * @throws SQLException
     */
    public void deleteMovieByID(int id) throws SQLException {
        try(Connection connection = dbConnection.getConnection()){
            String sql = "DELETE FROM Movie WHERE(id = ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        }
    }

    /**
     * adds movie to db based on given name, rating, absolutePath, and lastViewed
     * @param name
     * @param rating
     * @param absolutePath
     * @param lastViewed
     * @throws SQLException
     */
    public void addMovie(String name, double rating, String absolutePath, String lastViewed) throws SQLException {
        try(Connection connection = dbConnection.getConnection()){
            String sql = "INSERT INTO Movie(name, rating, absolute_path, last_viewed) VALUES(?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, rating);
            preparedStatement.setString(3, absolutePath);
            preparedStatement.setString(4, lastViewed);
            preparedStatement.execute();
        }
    }

    /**
     * updates movie with given id in db
     * @param id
     * @param name
     * @param rating
     * @param absolutePath
     * @param lastViewed
     * @throws SQLException
     */
    public void updateMovie(int id, String name, double rating, String absolutePath, String lastViewed) throws SQLException {
        try(Connection connection = dbConnection.getConnection()){
            String sql = "UPDATE Movie SET name = ?, rating = ? absolute_path = ? last_viewed = ? WHERE id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, rating);
            preparedStatement.setString(3, absolutePath);
            preparedStatement.setString(4, lastViewed);
            preparedStatement.setInt(5, id);
            preparedStatement.execute();
        }
    }
}
