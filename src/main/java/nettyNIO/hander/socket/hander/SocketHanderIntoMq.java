package nettyNIO.hander.socket.hander;

import com.alibaba.fastjson.JSONObject;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import nettyNIO.hander.common.KafkaProducerUtil;

public class SocketHanderIntoMq extends ChannelInboundHandlerAdapter {

    private KafkaProducerUtil kafkaProducerUtil = new KafkaProducerUtil("");

    /**
     * 接收过滤之后的数据
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        
        if(msg instanceof JSONObject){
            //将数据推入kafka
            JSONObject json = (JSONObject)msg;
            kafkaProducerUtil.sendMessAge(json.toJSONString());
        }

    }

}