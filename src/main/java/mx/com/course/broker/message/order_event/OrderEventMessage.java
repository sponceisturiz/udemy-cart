package mx.com.course.broker.message.order_event;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class OrderEventMessage {
    private long userId;
    private String operationType;
    private List<Long> coursesList;
}
