package utils;

public class CashManagerException extends Exception{

    public CashManagerException(String name){
        super(name);
    }

    public CashManagerException(CashManagerErrorType errorType){
        super(errorType.getName());
    }

}
