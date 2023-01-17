package gui.model;

import be.Category;
import be.Movie;
import bll.CategoryManager;
import bll.InputManager;
import gui.controller.AlertNotification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class CategoryModel {
    private final ObservableList<Category> categories;
    private ObservableList<Movie> moviesInCategories;
    private ObservableList<Category> missingCategories;
    CategoryManager bll = new CategoryManager();
    InputManager im = new InputManager();

    public CategoryModel() {
        categories = FXCollections.observableArrayList();
        fetchAllCategories();
    }

    public void fetchAllCategories() {
        categories.clear();
        try {
            categories.addAll(bll.getAllCategories());
        } catch (SQLException e) {
            AlertNotification.showAlertWindow(e.getMessage());
            throw new RuntimeException();
        }
    }

    public ObservableList<Category> getCategories() {
        return categories;
    }

    public void addCategory(String name) {
        try {
            bll.addCategory(name);
        } catch (SQLException e) {
            AlertNotification.showAlertWindow(e.getMessage());
            throw new RuntimeException();
        }

    }

    public void addMovieToCategory(int category_id, int movie_id) {
        try {
            bll.addMovieToCategory(category_id, movie_id);
        } catch (SQLException e) {
            AlertNotification.showAlertWindow(e.getMessage());
            throw new RuntimeException();
        }
    }

    public void setMoviesInCategory(int id) {
        try {
            moviesInCategories = FXCollections.observableArrayList(bll.getMoviesInCategories(id));
        } catch (SQLException e) {
            AlertNotification.showAlertWindow(e.getMessage());
            throw new RuntimeException();
        }
    }

    public void setMissingCategoriesInMovie(int id)
    {
        try {
            missingCategories = FXCollections.observableArrayList(bll.getMissingCategories(id));
        } catch (SQLException e) {
            AlertNotification.showAlertWindow(e.getMessage());
            throw new RuntimeException();
        }
    }

    public ObservableList<Movie> getMoviesInCategory() {
        return moviesInCategories;
    }

    public ObservableList<Category> getMissingCategories()
    {
        return missingCategories;
    }

    public void removeCategory(int id) {
        try {
            bll.removeCategory(id);
        } catch (SQLException e) {
            AlertNotification.showAlertWindow(e.getMessage());
            throw new RuntimeException();
        }
    }
    public boolean isCategoryDuplicate(String title)
    {
        return im.isCategoryDuplicate(title);
    }
}
