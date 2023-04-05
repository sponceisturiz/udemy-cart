package mx.com.course.exception.custom;

public class CourseAlreadyInCartException extends Exception{
    private static final long serialVersionUID = 11L;
    public CourseAlreadyInCartException(String message){
        super(message);
    }

    public CourseAlreadyInCartException(){
        super();
    }
}
