package mx.com.course.broker.consumer.cart;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mx.com.course.actions.CartReplyAction;
import mx.com.course.broker.message.cart.CartReplyMessage;
import mx.com.course.broker.message.cart.sync.CartSyncMessage;
import mx.com.course.exception.custom.CartIsEmptyException;
import mx.com.course.service.CartsService;

@Slf4j
@Component
@SuppressWarnings({"finally"})
@KafkaListener(topics = "#{'${udemy.topics.sync.t-cart.request}'.split(',')}")
public class CartConsumer {

    @Autowired
    CartsService cartsService;

    @Autowired
    CartReplyAction cartReplyAction;

    @KafkaHandler
    @SendTo("${udemy.topics.sync.t-cart.reply}")
	public CartReplyMessage listen(CartSyncMessage message) {
		log.info("Received request from Checkout: {}", message);
        List<Long> courseIdList = null;
        try{
            courseIdList = cartsService.findUserCartForMessaging(message.getUserId());
        }catch(CartIsEmptyException cise){
            courseIdList = Collections.emptyList();
        } finally {
            return cartReplyAction.publishReply(message.getUserId(), courseIdList);
        }
	}
}
