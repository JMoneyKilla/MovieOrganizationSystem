package dal;

import be.Category;
import be.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryDAO {
    private PreparedStatement preparedStatement;
    DBConnection dbConnection = new DBConnection();
    MovieDAO movieDAO = new MovieDAO();

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
     * retrieves category by given id
     *
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
     *
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
     * stores movie_id and category_id to CatMovie table in db
     *
     * @param categoryId
     * @param movieId
     * @throws SQLException
     */
    public void addCategoryToMovie(int categoryId, int movieId) throws SQLException {
        try (Connection connection = dbConnection.getConnection()) {
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
     *
     * @param categoryId
     * @return list of movies with specific category id
     * @throws SQLException
     */
    public List<Movie> getMovieByCategory(int categoryId) throws SQLException {
        List<Movie> moviesByCategory = new ArrayList<>();
        Movie movie;
        try (Connection connection = dbConnection.getConnection()) {
            String sql = "SELECT \n" +
                    "    movie_id,\n" +
                    "    movie_title,\n" +
                    "    user_rating,\n" +
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
            while (rs.next()) {
                int id = rs.getInt("movie_id");
                String name = rs.getString("movie_title");
                String rating = rs.getString("user_rating");
                String absolutePath = rs.getString("absolute_path");
                String lastViewed = rs.getString("last_viewed");
                String imdbRating = rs.getString("last_viewed");
                movie = new Movie(id, name, rating, absolutePath, lastViewed, imdbRating);
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

    public void addMovieToCategory(int category_id, int movie_id) {
        String sql = "INSERT INTO CatMovie (category_id, movie_id) VALUES (?,?)";

        try (Connection con = dbConnection.getConnection();) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, category_id);
            ps.setInt(2, movie_id);
            ps.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Movie getMovieByID(int id) throws SQLException {
        List<Movie> movies = movieDAO.getAllMovies();
        for (Movie m : movies
        ) {
            if (m.getId() == id)
                return m;
        }
        return null;
    }

    /**
     * We get movie ids depending on the chosen category id and adds them to a list.
     * @param category_id
     * @return list of movie ids.
     */

    public List<Integer> getMovieIdsFromCategory(int category_id) {
        List<Integer> movieIds = new ArrayList<>();
        String sql = "SELECT movie_id FROM CatMovie WHERE category_id =?";
        try (Connection con = dbConnection.getConnection();) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, category_id);
            ResultSet rst = ps.executeQuery();
            while (rst.next()) {
                movieIds.add(rst.getInt("movie_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return movieIds;
    }

    /**
     * We take the list of movie ids, we get depending on the category id.
     * In order to get the movie object, we create a loop that goes through all the ids we got from the database and the ids we have in our code.
     * It checks which ids are the same and adds them to a new list.
     * @param category_id
     * @return
     * @throws SQLException
     */
    public List<Movie> getMoviesFromCategory(int category_id) throws SQLException {
        List<Integer> movieIds = getMovieIdsFromCategory(category_id);
        List<Movie> moviesInCategory = new ArrayList<>();

        for (int id : movieIds) {
            moviesInCategory.add(getMovieByID(id));
        }
        return moviesInCategory;
    }

    public List<Integer> getCategoryIdsFromMovie(int movie_id) {
        List<Integer> categoryIds = new ArrayList<>();
        String sql = "SELECT category_id FROM CatMovie WHERE movie_id =?";
        try (Connection con = dbConnection.getConnection();) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, movie_id);
            ResultSet rst = ps.executeQuery();
            while (rst.next()) {
                categoryIds.add(rst.getInt("category_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categoryIds;
    }
    public List<Category> getCategoriesFromMovies(int movie_id) throws SQLException {
        List<Integer> categoryIds = getCategoryIdsFromMovie(movie_id);
        List<Category> categoriesInMovie = new ArrayList<>();

        for (int id : categoryIds) {
            categoriesInMovie.add(getCategoryByID(id));
        }
        return categoriesInMovie;
    }

    public List<Category> getMissingCategories(int movie_id) throws SQLException {
        List<Category> allCategories = getAllCategories();
        List<Category> categoriesInMovie = getCategoriesFromMovies(movie_id);
        List<Category> missingCategories = new ArrayList<>();

        for (Category c : categoriesInMovie) {
            for (Category ca : allCategories) {
                if (c.getId()==ca.getId()) {
                    allCategories.remove(ca);
                }
            }
        }
        //missingCategories.addAll(allCategories);
        return allCategories;
    }

    public static void main(String[] args) throws SQLException {
        CategoryDAO categoryDAO = new CategoryDAO();
        System.out.println(categoryDAO.getCategoriesFromMovies(2));
        System.out.println(categoryDAO.getCategoryIdsFromMovie(2));
        System.out.println(categoryDAO.getMissingCategories(2));
    }


    public void removeMovieFromCategory(Movie movie) {
        int id = movie.getId();
        String sql = "DELETE FROM CatMovie WHERE movie_id='" + id + "';";
        try (Connection con = dbConnection.getConnection();) {
            con.createStatement().execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
