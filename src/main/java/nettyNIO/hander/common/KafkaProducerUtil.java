package nettyNIO.hander.common;

import java.util.Properties;
import java.util.Set;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.serialization.StringSerializer;

import nettyNIO.hander.common.callback.KafkaCallback;

public class KafkaProducerUtil {
    
    private Properties props = new Properties();

    /**
     * bin/kafka-topics.sh --bootstrap-server kafka服务器地址:kafka端口
     * --create --topic主题名称 
     * --partitions 分区数目最优与broke数目一致
     * --replication-factor 分区数目最优与broke数目一致 
     * --config x=y
     *  */ 

    public KafkaProducerUtil(String server){
        //kafka服务器地址
        props.put("bootstrap.servers", server);
        //设置数据key和value的序列化处理类
        props.put("key.serializer", StringSerializer.class);
        props.put("value.serializer", StringSerializer.class);
        //3 acks
        // -1 代表所有处于isr列表中的follower partition都会同步写入消息成功
        // 0 代表消息只要发送出去就行，其他不管
        // 1 代表发送消息到leader partition写入成功就可以
        props.put(ProducerConfig.ACKS_CONFIG, "0");
        //4 重试次数
		props.put(ProducerConfig.RETRIES_CONFIG, 3);
		//5 隔多久重试一次
		props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 2000);
    }

    /**
     * 生产者发送指定主题数据
     * @param message 发送数据
     * @param jsonId 信息唯一编码
     * @param topic 发送主题
     */
    public void sendMessAge(String message,String jsonId,String topic){
        //<1> 若指定Partition ID,则PR被发送至指定Partition
        //<2> 若未指定Partition ID,但指定了Key, PR会按照hasy(key)发送至对应Partition
        //<3> 若既未指定Partition ID也没指定Key，PR会按照round-robin模式发送到每个Partition
        //<4> 若同时指定了Partition ID和Key, PR只会发送到指定的Partition (Key不起作用，代码逻辑决定)
        try(Producer<String, String> producer = new KafkaProducer<>(props)){
            ProducerRecord<String, String> prod =  new ProducerRecord<String, String>(topic, jsonId, message);
            producer.send(prod,new KafkaCallback());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 生产者发送指定主题数据
     * @param message 发送数据
     * @param topic 发送主题
     */
    public void sendMessAge(String message,String topic){
        //<1> 若指定Partition ID,则PR被发送至指定Partition
        //<2> 若未指定Partition ID,但指定了Key, PR会按照hasy(key)发送至对应Partition
        //<3> 若既未指定Partition ID也没指定Key，PR会按照round-robin模式发送到每个Partition
        //<4> 若同时指定了Partition ID和Key, PR只会发送到指定的Partition (Key不起作用，代码逻辑决定)
        try(Producer<String, String> producer = new KafkaProducer<>(props)){
            ProducerRecord<String, String> prod =  new ProducerRecord<String, String>(topic, message);
            producer.send(prod,new KafkaCallback());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 向所有主题发送信息
     * @param message 发送数据
     */
    public void sendMessAge(String message){
        
        AdminClient admin = KafkaAdminClient.create(props);
        ListTopicsResult topics = admin.listTopics();
        KafkaFuture<Set<String>> nameSet = topics.names();
        try(Producer<String, String> producer = new KafkaProducer<>(props)){
            for(String topicName:nameSet.get()){
                ProducerRecord<String, String> prod =  new ProducerRecord<String, String>(topicName, message);
                producer.send(prod,new KafkaCallback());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
