package domain;

public enum TransactionType {

    WITHDRAW ("Withdraw"),
    DEPOSIT ("Deposit"),
    TRANSFER ("Transfer");

    private String name;

    TransactionType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
