package controller.property_editor;

import domain.Currency;
import service.CurrencyService;

import java.beans.PropertyEditorSupport;

public class CurrencyPropertyEditor extends PropertyEditorSupport{

    public CurrencyPropertyEditor(CurrencyService dbCurrencyManager) {
        this.dbCurrencyManager = dbCurrencyManager;
    }

    private CurrencyService dbCurrencyManager;

    public String getAsText() {
        Currency obj = (Currency) getValue();
        if (null==obj) {
            return "";
        } else {
            return obj.getId().toString();
        }
    }

    public void setAsText(final String value) {
        try {
            Long id = Long.parseLong(value);
            Currency currency = dbCurrencyManager.get(id);
            if (null!=currency) {
                super.setValue(currency);
            } else {
                throw new IllegalArgumentException("Binding error. Cannot find currency with id  ["+value+"]");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Binding error. Invalid id: " + value);
        }
    }

}

