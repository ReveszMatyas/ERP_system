package Logic.exceptions;

public class ProductWithoutIdException extends RuntimeException{
    public String message;

    public ProductWithoutIdException(){
        super();
    }
    public ProductWithoutIdException(String message){
        super(message);
    }

    public ProductWithoutIdException(String message, Throwable cause){
        super(message, cause);
    }
}
