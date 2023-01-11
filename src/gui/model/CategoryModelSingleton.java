package gui.model;

public class CategoryModelSingleton {

    private static CategoryModelSingleton instance = null;

    private CategoryModel categoryModel = new CategoryModel();

    private CategoryModelSingleton(){}

    public static CategoryModelSingleton getInstance(){
        if(instance == null){
            instance = new CategoryModelSingleton();
        }
        return instance;
    }

    public CategoryModel getCategoryModel(){
        return categoryModel;
    }
}
