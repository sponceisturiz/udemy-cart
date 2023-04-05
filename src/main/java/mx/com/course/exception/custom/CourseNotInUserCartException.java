package mx.com.course.exception.custom;

public class CourseNotInUserCartException extends Exception{
    private static final long serialVersionUID = 12L;
    public CourseNotInUserCartException(String message){
        super(message);
    }

    public CourseNotInUserCartException(){
        super();
    }
}
