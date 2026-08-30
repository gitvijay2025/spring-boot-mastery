package com.example.mastery.config;

import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;


@Configuration
public class KafkaErrorConfig {


    @Bean
    public DefaultErrorHandler errorHandler(
            KafkaTemplate<Object, Object> kafkaTemplate) {

        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(kafkaTemplate);

        return new DefaultErrorHandler(
                recoverer,
                new FixedBackOff(3000L, 2)
        );
    }
}

//    @Bean
//    public DefaultErrorHandler errorHandler(KafkaTemplate<Object, Object> template) {
//        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
//                template,
//                // Do not require the DLT to have the same partition count as the source topic.
//                (record, exception) -> new TopicPartition(record.topic() + ".DLT", -1)
//        );
//
//        return new DefaultErrorHandler(
//                recoverer,
//                new FixedBackOff(3000L, 2)
//        );
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory(
//            ConsumerFactory<Object, Object> consumerFactory,
//            DefaultErrorHandler errorHandler) {
//
//        ConcurrentKafkaListenerContainerFactory<Object, Object> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory);
//        factory.setCommonErrorHandler(errorHandler);   // ✅ EXPLICITLY attach kiya
//        return factory;
//    }
//}

//@Configuration
//public class KafkaErrorConfig {
//    @Bean
//    public DefaultErrorHandler errorHandler(KafkaTemplate<Object, Object> template) {
//        // Publishes failed records to "<original-topic>.DLT" after retries
//        return new DefaultErrorHandler(
//                new DeadLetterPublishingRecoverer(template),
//                new FixedBackOff(3000L, 2) // Retry 2 times with 3s delay
//        );
//    }
//}
