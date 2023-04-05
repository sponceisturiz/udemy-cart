package mx.com.course.actions;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mx.com.course.broker.enumerable.MessageType;
import mx.com.course.broker.message.course.CoursePriceReplyMessage;
import mx.com.course.broker.message.course.sync.CourseSyncCartMessage;

@Slf4j
@Component
public class CourseAction {
    private String COURSE_SYNC_TOPIC = "";
    private String COURSE_SYNC_REPLY_CART_TOPIC = "";
    
    @Autowired
    @Qualifier("replyingCourse")
    ReplyingKafkaTemplate<String, CourseSyncCartMessage, CoursePriceReplyMessage> kafkaTemplate;

    public CourseAction(@Value("${udemy.topics.sync.t-course.request}") String courseSyncTopic,
                            @Value("${udemy.topics.sync.t-course.reply}") String courseSyncReplyCartTopic){
            COURSE_SYNC_TOPIC = courseSyncTopic;
            COURSE_SYNC_REPLY_CART_TOPIC = courseSyncReplyCartTopic;
    }

    public CoursePriceReplyMessage publishAndReply(long userId, List<Long> courseIdList) throws InterruptedException, ExecutionException{

        CourseSyncCartMessage courseMessage = new CourseSyncCartMessage(userId, MessageType.Request.CART.name(), courseIdList);

        ProducerRecord<String, CourseSyncCartMessage> record = new ProducerRecord<String,CourseSyncCartMessage>(COURSE_SYNC_TOPIC, courseMessage);

        //Set Reply Topic on Headers
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, COURSE_SYNC_REPLY_CART_TOPIC.getBytes()));

        RequestReplyFuture<String, CourseSyncCartMessage, CoursePriceReplyMessage> sendAndReceive = kafkaTemplate.sendAndReceive(record);

        //SendResult<String, UserCourseMessage> sendResult = sendAndReceive.getSendFuture().get();
        log.info("Requesting Course synchronously: {}", courseMessage);
        ConsumerRecord<String, CoursePriceReplyMessage> consumerRecord = sendAndReceive.get();

        log.info("Request completed: {}", consumerRecord.value());
        return consumerRecord.value();
    }
}
