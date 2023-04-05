package mx.com.course.broker.consumer.course_event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mx.com.course.broker.enumerable.OperationType;
import mx.com.course.broker.message.course_event.CourseEventMessage;
import mx.com.course.service.CartsService;

@Slf4j
@Component
public class CourseEventConsumer {
    
    @Autowired
    CartsService cartsService;

    @KafkaListener(topics = "#{'${udemy.topics.t-course-event}'.split(',')}")
	public void listen(CourseEventMessage message) {
        log.info("Received event from Course of type [ {} ]: {}", message.getOperationType(), message);

        if(message.getOperationType().equals(OperationType.Event.DELETE.name())){
            //delete course in all carts
            cartsService.removeCourseFromAllCarts(message.getCourseId());
        }
    }
}
