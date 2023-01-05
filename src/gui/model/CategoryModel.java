package gui.model;

import be.Category;
import be.Movie;
import bll.CategoryManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CategoryModel {
    private final ObservableList<Category> categories;
    private final ObservableList<Movie> moviesInCategories;
    CategoryManager bll = new CategoryManager();

    public CategoryModel() {
        categories = FXCollections.observableArrayList();
        moviesInCategories = FXCollections.observableArrayList();
    }

    public void fetchAllCategories() {
        categories.clear();
        categories.addAll(bll.getAllCategories());
    }

    public ObservableList<Category> getCategories() {
        return categories;
    }

    public void addCategory(String name) {
        bll.addCategory(name);
        int id = bll.getNewestCategoryId();
        Category category = new Category(id, name);
        categories.add(category);
    }
}

