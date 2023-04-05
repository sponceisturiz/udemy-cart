package mx.com.course.broker.message.course.sync;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class CourseSyncCartMessage {
    private long followingId;
    private String requestType;
    private List<Long> courseIdList;
    
}
