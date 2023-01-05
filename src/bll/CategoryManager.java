package bll;

import be.Category;
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
}
