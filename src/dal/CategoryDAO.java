package dal;

import be.Category;
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
        try(Connection connection = dbConnection.getConnection()){
            String sql ="SELECT id, name FROM Category";
            preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                category = new Category(id, name);
                retrievedCategories.add(category);
            }
        }
        return retrievedCategories;
    }

    public Category getCategoryByID(int id) throws SQLException {
        List<Category> categories = getAllCategories();
        for (Category c: categories
             ) {
            if(c.getId() == id)
                return c;
        }
        return null;
    }

    public void deleteCategoryByID(int id) throws SQLException {
        try(Connection connection = dbConnection.getConnection()){
            String sql = "DELETE FROM category WHERE(id = ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        }
    }

    public void addCategory(String name) throws SQLException {
        try(Connection connection = dbConnection.getConnection()){
            String sql = "INSERT INTO Category(name) VALUES(?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.execute();
        }
    }
}
