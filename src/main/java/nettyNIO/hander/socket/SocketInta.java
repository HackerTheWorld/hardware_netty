package nettyNIO.hander.socket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.json.JsonObjectDecoder;
import nettyNIO.hander.socket.hander.SocketFilterHander;
import nettyNIO.hander.socket.hander.SocketHanderBackUpToData;

/**
 * 初始化通道
 */
public class SocketInta extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel channel) throws Exception {
        //特殊字符解码器
//        ByteBuf buf = Unpooled.copiedBuffer("//".getBytes());
//        channel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,buf));
        // 自定义解码
        // channel.pipeline().addLast(new SocketDecoder());
        // 自定义编码
        //channel.pipeline().addLast(new StringEncoder());
        channel.pipeline().addLast(new JsonObjectDecoder());
        channel.pipeline().addLast(new SocketFilterHander());
        channel.pipeline().addLast(new SocketHanderBackUpToData());
    }
}
