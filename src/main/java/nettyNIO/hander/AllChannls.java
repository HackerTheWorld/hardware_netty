package nettyNIO.hander;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import java.util.concurrent.ConcurrentHashMap;

public class AllChannls {

    public static ConcurrentHashMap<ChannelId, ChannelHandlerContext> channelMap ;

    public static ConcurrentHashMap<ChannelId,ChannelHandlerContext> webChanelMap;

    static {
        channelMap = new ConcurrentHashMap<ChannelId, ChannelHandlerContext>();
        webChanelMap = new ConcurrentHashMap<ChannelId, ChannelHandlerContext>();
    }

}
