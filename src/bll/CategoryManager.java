package bll;

import be.Category;
import be.Movie;
import dal.CategoryDAO;

import java.sql.SQLException;
import java.util.List;

public class CategoryManager {
    CategoryDAO categoryDAO = new CategoryDAO();

    public List<Category> getAllCategories() throws SQLException {
        return categoryDAO.getAllCategories();
    }
    public List<Movie> getMoviesInCategories(int id) throws SQLException {
        return categoryDAO.getMoviesFromCategory(id);
    }

    public List<Category> getMissingCategories(int id) throws SQLException {
        return categoryDAO.getMissingCategories(id);
    }

    public void addCategory(String name) throws SQLException {
        categoryDAO.addCategory(name);
    }

    public int getNewestCategoryId() throws SQLException {
        return categoryDAO.getNewestCategoryId();
    }

    public void addMovieToCategory(int category_id, int movie_id) throws SQLException {
        categoryDAO.addMovieToCategory(category_id, movie_id);
    }

    public void selectCategory(int id) throws SQLException {
        categoryDAO.getMoviesFromCategory(id);
    }

    public Movie getMovie(int movie_id) throws SQLException {
        return categoryDAO.getMovieByID(movie_id);
    }

    public Category getCategory(int category_id) throws SQLException {
        return categoryDAO.getCategoryByID(category_id);
    }

    public void selectMovie(int id) throws SQLException {
        categoryDAO.getMovieByID(id);
    }

    public void removeCategory(int id) throws SQLException {
        categoryDAO.removeCategoryFromCatMovie(id);
        categoryDAO.deleteCategoryByID(id);
    }
}
