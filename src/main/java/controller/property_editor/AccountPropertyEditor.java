package controller.property_editor;

import domain.Account;
import service.AccountService;

import java.beans.PropertyEditorSupport;

public class AccountPropertyEditor extends PropertyEditorSupport{
    public AccountPropertyEditor(AccountService dbAccountManager) {
        this.dbAccountManager = dbAccountManager;
    }

    private AccountService dbAccountManager;

    public String getAsText() {
        Account obj = (Account) getValue();
        if (null == obj) {
            return "";
        } else {
            return obj.getId().toString();
        }
    }

    public void setAsText(final String value) {
        try {
            Long id = Long.parseLong(value);
            Account account = dbAccountManager.get(id);
            super.setValue(account);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Binding error. Invalid id: " + value);
        }
    }

}
