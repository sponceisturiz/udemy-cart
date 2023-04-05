package mx.com.course.exception.custom;

public class CartIsEmptyException extends Exception{
    private static final long serialVersionUID = 10L;
    public CartIsEmptyException(String message){
        super(message);
    }

    public CartIsEmptyException(){
        super();
    }
}
