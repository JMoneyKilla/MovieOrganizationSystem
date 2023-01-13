package gui.model;

import be.Category;
import be.Movie;
import bll.CategoryManager;
import gui.controller.AlertNotification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class CategoryModel {
    private final ObservableList<Category> categories;
    private ObservableList<Movie> moviesInCategories;
    private ObservableList<Category> missingCategories;
    CategoryManager bll = new CategoryManager();

    public CategoryModel() {
        categories = FXCollections.observableArrayList();
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
            int id = bll.getNewestCategoryId();
            Category category = new Category(id, name);
            categories.add(category);
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

    public void selectCategory(int id) {
        try {
            bll.selectCategory(id);
            moviesInCategories = FXCollections.observableArrayList(bll.getMoviesInCategories(id));
        } catch (SQLException e) {
            AlertNotification.showAlertWindow(e.getMessage());
            throw new RuntimeException();
        }
    }

    public void selectMovie(int id)
    {
        try {
            bll.selectMovie(id);
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
}

