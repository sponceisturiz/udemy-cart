package mx.com.course.broker.message.course;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.com.course.model.CoursePriceVO;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class CoursePriceReplyMessage {
    private long followingId;
    private String responseType;
    private List<CoursePriceVO> courseIdList;
}
