package mx.com.course.actions;

import java.util.List;

import org.springframework.stereotype.Component;

import mx.com.course.broker.message.cart.CartReplyMessage;

@Component
public class CartReplyAction {

    public CartReplyMessage publishReply(long userId, List<Long> courseIdList){
        return new CartReplyMessage(userId, courseIdList);
    }
}
