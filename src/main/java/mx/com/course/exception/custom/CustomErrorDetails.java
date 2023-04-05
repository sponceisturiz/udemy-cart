package mx.com.course.exception.custom;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class CustomErrorDetails {
    
    private Date timestamp;
    private String message;
    private String errorDetails;

}
