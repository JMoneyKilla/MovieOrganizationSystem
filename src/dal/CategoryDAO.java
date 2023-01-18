package dal;

import be.Category;
import be.Movie;

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
     *
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
     * Deletes category from table CatMovie based on the id, that has been selected.
     *
     * @param id
     */
    public void removeCategoryFromCatMovie(int id) throws SQLException {
        String sql = "DELETE FROM CatMovie WHERE category_id='" + id + "';";
        try (Connection con = dbConnection.getConnection();) {
            con.createStatement().execute(sql);
        }
    }

    /**
     * deletes category from db by given categoryId
     *
     * @param id
     * @throws SQLException
     */
    public void deleteCategoryByID(int id) throws SQLException {
        try (Connection connection = dbConnection.getConnection()) {
            String sql = "DELETE FROM Category WHERE(id = ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        }
    }


    /**
     * creates new category and adds it to db
     *
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
     * Joins the Movie and Category tables on the CatMovie table.
     * Searches through joined table for movies with specific category id and adds them to a list.
     * Used to organize movies into different categories and allows one movie to have multiple
     * categories.
     *
     * @param categoryId
     * @return list of movies with specific category id
     * @throws SQLException
     */
    public List<Movie> getMovieByCategory(int categoryId) throws SQLException {
        List<Movie> moviesByCategory = new ArrayList<>();
        Movie movie;
        try (Connection connection = dbConnection.getConnection()) {
            String sql = "SELECT * \n" +
                    "FROM Movie m \n" +
                    "INNER JOIN CatMovie cm \n" +
                    "    ON m.id = cm.movie_id\n" +
                    "RIGHT JOIN Category c\n" +
                    "    ON cm.category_id = c.id\n" +
                    "WHERE category_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, categoryId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("movie_id");
                String name = rs.getString("movie_title");
                String rating = rs.getString("user_rating");
                String absolutePath = rs.getString("absolute_path");
                String lastViewed = rs.getString("last_viewed");
                String imdbRating = rs.getString("imdb_rating");
                movie = new Movie(id, name, rating, absolutePath, lastViewed, imdbRating);
                moviesByCategory.add(movie);
            }
        }
        return moviesByCategory;
    }


    /**
     * We get category_id and movie_id based on what has been selected in the program.
     * We then add them to table CatMovie.
     *
     * @param category_id
     * @param movie_id
     */
    public void addMovieToCategory(int category_id, int movie_id) throws SQLException {
        String sql = "INSERT INTO CatMovie (category_id, movie_id) VALUES (?,?)";

        try (Connection con = dbConnection.getConnection();) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, category_id);
            ps.setInt(2, movie_id);
            ps.execute();

        }
    }


    /**
     * Joins Category and Movie tables on CatMovie.
     * Takes category_name and category_id based on movie_id to return list of categories in movie
     * @param movieId
     * @return List of categories in specific movie
     * @throws SQLException
     */
    public List<Category> getCategoriesByMovieId(int movieId) throws SQLException {
        List<Category> categoriesByMovie = new ArrayList<>();
        Category category;
        try (Connection connection = dbConnection.getConnection()) {
            String sql = " SELECT *\n" +
                    "        FROM Category c\n" +
                    "        INNER JOIN CatMovie cm\n" +
                    "            ON c.id = cm.category_id\n" +
                    "        RIGHT JOIN Movie m\n" +
                    "            ON cm.movie_id = m.id\n" +
                    "        WHERE movie_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, movieId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("category_id");
                String name = rs.getString("category_name");
                category = new Category(id, name);
                categoriesByMovie.add(category);
            }
        }
        return categoriesByMovie;
    }

    /** We have a list of all categories and the categories in a specific movie.
     * We remove if the categoryInMovie matches any category in the list of all categories.
     * We then return a list of the missing categories in the specific movie.
     * @param movie_id
     * @return list of missing categories.
     * @throws SQLException
     */
    public List<Category> getMissingCategories(int movie_id) throws SQLException {
        List<Category> allCategories = getAllCategories();
        List<Category> categoriesInMovie = getCategoriesByMovieId(movie_id);
        List<Category> missingCategories = new ArrayList<>(allCategories);

        missingCategories.removeIf(category -> categoriesInMovie.stream().anyMatch(category1 -> category1.getId() == category.getId()));
        return missingCategories;
    }
}