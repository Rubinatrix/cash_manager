package utils;

public enum CashManagerErrorType {

    CURRENCY_DELETE("Removing currency isn't allowed - it's currently in use"),
    CATEGORY_DELETE("Removing category isn't allowed - it's currently in use"),
    TRANSFER_CROSS_CURRENCY("Can't save transfer to account with another currency"),
    USER_EXISTS("An account for that username already exists. Please enter a different username!");

    private String name;

    CashManagerErrorType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
