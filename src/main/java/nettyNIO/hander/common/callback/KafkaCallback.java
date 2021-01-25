package nettyNIO.hander.common.callback;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

public class KafkaCallback implements Callback {

    /**
     * kafka发送结果回调函数
     */
    @Override
    public void onCompletion(RecordMetadata rec, Exception exce) {
        if(exce != null){
            exce.printStackTrace();
        }else{
            String topic = rec.topic();
            System.out.println(topic+"::success");
        }
    }
    
}
