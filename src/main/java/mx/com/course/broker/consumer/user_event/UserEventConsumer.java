package mx.com.course.broker.consumer.user_event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mx.com.course.broker.enumerable.OperationType;
import mx.com.course.broker.message.user_event.UserEventMessage;
import mx.com.course.service.CartsService;

@Slf4j
@Component
public class UserEventConsumer {
    
    @Autowired
    CartsService cartsService;

    @KafkaListener(topics = "#{'${udemy.topics.t-user-event}'.split(',')}")
	public void listen(UserEventMessage message) {
        log.info("Received event from User of type [ {} ]: {}", message.getOperationType(), message);

        if(message.getOperationType().equals(OperationType.Event.DELETE.name())){
            //delete user cart
            cartsService.removeUserCart(message.getUserId());
        }
    }
}
