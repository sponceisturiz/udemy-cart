package mx.com.course.broker.message.cart;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class CartReplyMessage {
    private long userId;
    private List<Long> courseIdList;
}
