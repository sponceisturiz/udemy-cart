package mx.com.course.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import mx.com.course.broker.message.course.CoursePriceReplyMessage;
import mx.com.course.broker.message.course.sync.CourseSyncCartMessage;

@Configuration
@Profile("default")
public class KafkaConfig {

    @Bean
    public KafkaTemplate<?,?> kafkaTemplate(ProducerFactory<String, ?> pf){
        return new KafkaTemplate<>(pf);
    }
    
    @Bean(name = "replyingCourse")
    public ReplyingKafkaTemplate<String, CourseSyncCartMessage, CoursePriceReplyMessage> replyingCourse(
            ProducerFactory<String, CourseSyncCartMessage> pf, ConcurrentKafkaListenerContainerFactory<String, CoursePriceReplyMessage> containerFactory,
            @Value("${spring.kafka.consumer.group-id}")String groupId, @Value("${udemy.topics.sync.t-course.reply}")String reply) {

        ConcurrentMessageListenerContainer<String, CoursePriceReplyMessage> repliesContainer = containerFactory.createContainer(reply);
        repliesContainer.getContainerProperties().setGroupId(groupId);
        repliesContainer.setAutoStartup(false);
        return new ReplyingKafkaTemplate<>(pf, repliesContainer);
    }
}
