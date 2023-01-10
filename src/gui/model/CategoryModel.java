package gui.model;

import be.Category;
import be.Movie;
import bll.CategoryManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

    public void addMovieToCategory(int category_id, int movie_id) {
        bll.addMovieToCategory(category_id, movie_id);
    }

    public void selectCategory(int id) {
        bll.selectCategory(id);
        moviesInCategories = FXCollections.observableArrayList(bll.getMoviesInCategories(id));
    }

    public void selectMovie(int id)
    {
        bll.selectMovie(id);
        missingCategories = FXCollections.observableArrayList(bll.getMissingCategories(id));
    }

    public ObservableList<Movie> getMoviesInCategory() {
        return moviesInCategories;
    }

    public ObservableList<Category> getMissingCategories()
    {
        return missingCategories;
    }

    public void removeMovieFromCategory(Movie selected) {
        bll.removeMovieFromCategory(selected);
        moviesInCategories.remove(selected);
    }
}

