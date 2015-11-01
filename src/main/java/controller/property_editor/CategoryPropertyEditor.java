package controller.property_editor;

import domain.Category;
import service.CategoryService;

import java.beans.PropertyEditorSupport;

public class CategoryPropertyEditor extends PropertyEditorSupport{

    public CategoryPropertyEditor(CategoryService dbCategoryManager) {
        this.dbCategoryManager = dbCategoryManager;
    }

    private CategoryService dbCategoryManager;

    public String getAsText() {
        Category obj = (Category) getValue();
        if (null == obj) {
            return "";
        } else {
            return obj.getId().toString();
        }
    }

    public void setAsText(final String value) {
        try {
            Long id = Long.parseLong(value);
            Category category = dbCategoryManager.get(id);
            super.setValue(category);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Binding error. Invalid id: " + value);
        }
    }
}
