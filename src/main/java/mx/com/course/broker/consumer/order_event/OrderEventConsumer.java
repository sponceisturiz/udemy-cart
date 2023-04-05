package mx.com.course.broker.consumer.order_event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mx.com.course.broker.enumerable.OperationType;
import mx.com.course.broker.message.order_event.OrderEventMessage;
import mx.com.course.service.CartsService;

@Slf4j
@Component
public class OrderEventConsumer {
    
    @Autowired
    CartsService cartsService;

    @KafkaListener(topics = "#{'${udemy.topics.t-order-event}'.split(',')}")
	public void listen(OrderEventMessage message) {
        log.info("Received event from Order of type [ {} ]: {}", message.getOperationType(), message);

        if(message.getOperationType().equals(OperationType.Event.CREATE.name())){
            //remove courses already owned by user
            cartsService.removeUserCart(message.getUserId());
        }
    }
}
