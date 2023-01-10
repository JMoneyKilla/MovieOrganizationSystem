package bll;

import be.Category;
import be.Movie;
import dal.CategoryDAO;

import java.sql.SQLException;
import java.util.List;

public class CategoryManager {
    CategoryDAO categoryDAO = new CategoryDAO();
    public List<Category> getAllCategories() {
        try {
            return categoryDAO.getAllCategories();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Movie> getMoviesInCategories(int id)
    {
        try {
            return categoryDAO.getMoviesFromCategory(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Category> getMissingCategories(int id)
    {
        try {
            return categoryDAO.getMissingCategories(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addCategory(String name) {
        try {
            categoryDAO.addCategory(name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getNewestCategoryId() {
        try {
            return categoryDAO.getNewestCategoryId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addMovieToCategory(int category_id, int movie_id) {
        categoryDAO.addMovieToCategory(category_id, movie_id);
    }

    public void selectCategory(int id) {
        try {
            categoryDAO.getMoviesFromCategory(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Movie getMovie(int movie_id) {
        try {
            return categoryDAO.getMovieByID(movie_id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Category getCategory(int category_id) {
        try {
            return categoryDAO.getCategoryByID(category_id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeMovieFromCategory(Movie selected) {
        categoryDAO.removeMovieFromCategory(selected);
    }

    public void selectMovie(int id) {
        try {
            categoryDAO.getMovieByID(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
