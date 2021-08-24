package nettyNIO.hander.socket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.json.JsonObjectDecoder;
import nettyNIO.hander.socket.hander.SocketFilterHander;
import nettyNIO.hander.socket.hander.SocketHanderBackUpToData;
import nettyNIO.hander.socket.hander.SocketHanderIntoMq;

/**
 * 初始化通道
 */
public class SocketInta extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel channel) throws Exception {
        //特殊字符解码器
        //ByteBuf buf = Unpooled.copiedBuffer("//".getBytes());
        //channel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,buf));
        // 自定义解码
        // channel.pipeline().addLast(new SocketDecoder());
        // 自定义编码
        //channel.pipeline().addLast(new StringEncoder());
        channel.pipeline().addFirst("jsonDecoder",new JsonObjectDecoder());
        channel.pipeline().addAfter("jsonDecoder","filter",new SocketFilterHander());
        channel.pipeline().addAfter("filter","backUp",new SocketHanderBackUpToData());
        channel.pipeline().addLast("intoMq",new SocketHanderIntoMq());
    }
}
