package dal;

import be.Category;
import be.Movie;
import com.sun.net.httpserver.Authenticator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private PreparedStatement preparedStatement;
    DBConnection dbConnection = new DBConnection();

    /**
     * retrieves all category types from db
     * @return List of all category types
     * @throws SQLException
     */
    public List<Category> getAllCategories() throws SQLException {
        Category category;
        List<Category> retrievedCategories = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection()) {
            String sql = "SELECT id, category_name FROM Category";
            preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("category_name");
                category = new Category(id, name);
                retrievedCategories.add(category);
            }
        }
        return retrievedCategories;
    }

    /**
     * retrieves category by given id
     * @param id
     * @return
     * @throws SQLException
     */
    public Category getCategoryByID(int id) throws SQLException {
        List<Category> categories = getAllCategories();
        for (Category c : categories
        ) {
            if (c.getId() == id)
                return c;
        }
        return null;
    }

    /**
     * deletes category from db by given categoryId
     * @param id
     * @throws SQLException
     */
    public void deleteCategoryByID(int id) throws SQLException {
        try (Connection connection = dbConnection.getConnection()) {
            String sql = "DELETE FROM category WHERE(id = ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        }
    }

    /**
     * creates new category and adds it to db
     * @param name
     * @throws SQLException
     */
    public void addCategory(String name) throws SQLException {
        try (Connection connection = dbConnection.getConnection()) {
            String sql = "INSERT INTO Category(category_name) VALUES(?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.execute();
        }
    }

    /**
     * stores movie_id and category_id to CatMovie table in db
     * @param categoryId
     * @param movieId
     * @throws SQLException
     */
    public void addCategoryToMovie(int categoryId, int movieId) throws SQLException {
        try(Connection connection = dbConnection.getConnection()){
            String sql = "INSERT INTO CatMovie(category_id, movie_id) VALUES(?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, categoryId);
            preparedStatement.setInt(2, movieId);
            preparedStatement.execute();
        }
    }

    /**
     * Joins the Movie and Category tables on the CatMovie table.
     * Searches through joined table for movies with specific category id and adds them to a list.
     * Used to organize movies into different categories and allows one movie to have multiple
     * categories.
     * @param categoryId
     * @return list of movies with specific category id
     * @throws SQLException
     */
    public List<Movie> getMovieByCategory(int categoryId) throws SQLException {
        List<Movie> moviesByCategory = new ArrayList<>();
        Movie movie;
        try(Connection connection = dbConnection.getConnection()){
            String sql = "SELECT \n" +
                    "    movie_id,\n" +
                    "    movie_title,\n" +
                    "    rating,\n" +
                    "    absolute_path,\n" +
                    "    last_viewed\n" +
                    "FROM Movie m \n" +
                    "INNER JOIN CatMovie cm \n" +
                    "    ON m.id = cm.movie_id\n" +
                    "RIGHT JOIN Category c\n" +
                    "    ON cm.category_id = c.id\n" +
                    "WHERE category_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, categoryId);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                int id = rs.getInt("movie_id");
                String name = rs.getString("movie_title");
                double rating = rs.getDouble("rating");
                String absolutePath = rs.getString("absolute_path");
                String lastViewed = rs.getString("last_viewed");
                movie = new Movie(id, name, rating, absolutePath, lastViewed);
                moviesByCategory.add(movie);
            }
        }
        return moviesByCategory;
    }

    public int getNewestCategoryId() throws SQLException {
        try (Connection con = dbConnection.getConnection()) {
            ResultSet rs = con.createStatement().executeQuery("SELECT TOP 1 * FROM Category ORDER BY id DESC;");
            rs.next();
            int id = rs.getInt("id");
            int nextID = id;
            return nextID;
        }
    }
}
