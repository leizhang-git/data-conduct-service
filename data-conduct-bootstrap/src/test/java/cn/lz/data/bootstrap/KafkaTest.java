package cn.lz.data.bootstrap;


import com.google.common.collect.Lists;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Desc
 * @Author zhanglei
 * @Date 2023/12/29 14:00
 */
public class KafkaTest {

    public static void main(String[] args) {
//        Properties properties = getProperties();
//        String topicName = "test-Q";
//        createTopic(properties, topicName);
//        //生产一万条数据
//        KafkaProducer<String, String> producer = getProducer(properties);
//        for (int i = 0; i < 10000; i++) {
//            async(producer, topicName, null, String.valueOf(i));
//        }
//        producer.close();

        //--------------------------------
        Properties properties = getProperties2();
        String topicName = "test-Q";
        KafkaConsumer<String, String> consumer = getConsumer(properties);
        subTopic(consumer, topicName);
        while (true) {
            getResult(consumer);
        }
    }

    public static Properties getProperties() {
        Properties props = new Properties();
        //Kafka服务器地址
        props.put("bootstrap.servers", "192.168.25.100:9092");
        //acks表示生产者生产数据到kafka中，kafka中以什么样的策略返回
        //0：生产者不会等待kafka响应，1：kafka会把消息写道本地日志文件中，不会等待集群中其他机器响应，all：leader会等所有follower同步完成
        props.put("acks", "all");
        //以字符串方式进行序列化
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());
        return props;
    }

    public static Properties getProperties2() {
        Properties props = new Properties();
        //Kafka服务器地址
        props.put("bootstrap.servers", "192.168.25.100:9092");
        //消费者组（可以使用消费者组将若干消费者组织到一起），共同消费Kafka中的topic的数据
        props.setProperty("group.id", "test-QQ");
        props.setProperty("auto.offset.reset", "earliest");
        //自动提交offset
        props.setProperty("enable.auto.commit", "true");
        //自动提交offset的时间间隔
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("max.poll.records", "20000");
        //以字符串方式进行序列化
        props.setProperty("key.deserializer", StringDeserializer.class.getName());
        props.setProperty("value.deserializer", StringDeserializer.class.getName());
        return props;
    }

    public static void createTopic(Properties props, String topicName) {
        try (AdminClient adminClient = KafkaAdminClient.create(props)) {
            Set<String> strings = adminClient.listTopics().names().get();
            if (strings.contains(topicName)) {
                return;
            }
            NewTopic newTopic = new NewTopic("test-Q", 3, (short) 1);
            CreateTopicsResult result = adminClient.createTopics(Lists.newArrayList(newTopic));
            result.all().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static KafkaProducer<String, String> getProducer(Properties props) {
        return new KafkaProducer<>(props);
    }

    public static KafkaConsumer<String, String> getConsumer(Properties props) {
        return new KafkaConsumer<>(props);
    }

    public static void subTopic(KafkaConsumer consumer, String topicName) {
        consumer.subscribe(Collections.singletonList(topicName));
    }

    public static void syncSend(KafkaProducer kafkaProducer, String topic, String key, String value) throws ExecutionException, InterruptedException {
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, value);
        Future<RecordMetadata> future = kafkaProducer.send(producerRecord);
        future.get();
        System.out.println("消息写入成功！");
    }

    public static void async(KafkaProducer kafkaProducer, String topic, String key, String value) {
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, value);
        kafkaProducer.send(producerRecord, (recordMetadata, e) -> {
            //1.判断发送消息是否成功
            if(e != null) {
                System.out.println("生产消息出现异常！");
                System.out.println(e.getMessage());
                System.out.println(Arrays.toString(e.getStackTrace()));
            }else {
                String topicResult = recordMetadata.topic();
                int partition = recordMetadata.partition();
                long offset = recordMetadata.offset();
                System.out.println("topic: " + topicResult + "partition: " + partition + "offset: " + offset);
            }
        });
    }

    public static void getResult(KafkaConsumer consumer) {
        ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(5));
        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
            String topic = consumerRecord.topic();
            //offset
            long offset = consumerRecord.offset();
            //key
            String key = consumerRecord.key();
            //value
            String value = consumerRecord.value();
            System.out.println("====================================================================================topic: " + topic + " offset: " + offset + " key: " + key + " value: " + value);
        }
    }

    public static void closeKafka(KafkaProducer kafkaProducer) {
        kafkaProducer.close();
    }
}
