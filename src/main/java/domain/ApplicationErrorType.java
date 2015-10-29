package domain;

public enum ApplicationErrorType {

    CURRENCY_DELETE("Removing currency isn't allowed - it's currently in use"),
    CATEGORY_DELETE("Removing category isn't allowed - it's currently in use"),
    TRANSFER_CROSS_CURRENCY("Can't save transfer to wallet with another currency"),
    USER_EXISTS("An account for that username already exists. Please enter a different username!");

    private String name;

    ApplicationErrorType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
