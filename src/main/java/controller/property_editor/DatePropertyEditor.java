package controller.property_editor;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class DatePropertyEditor extends PropertyEditorSupport{
    private final DateFormat dateFormat;

    public DatePropertyEditor(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getAsText() {
        Date value = (Date) getValue();
        return value != null ? this.dateFormat.format(value) : "";
    }

    public void setAsText(String text) throws IllegalArgumentException {
        try {
            Date date = this.dateFormat.parse(text);
            super.setValue(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Could not parse date: " + e.getMessage(), e);
        }
    }
}
