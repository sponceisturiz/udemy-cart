package mx.com.course.broker.message.course_event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class CourseEventMessage {
    private long courseId;
    private String operationType;
}
