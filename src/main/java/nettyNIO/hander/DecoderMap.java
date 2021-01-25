package nettyNIO.hander;

import io.netty.channel.ChannelId;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class DecoderMap {

    public static ConcurrentHashMap<ChannelId, ArrayList<ArrayList<Byte>>> decoderMap ;

    static {
        decoderMap = new ConcurrentHashMap<ChannelId,ArrayList<ArrayList<Byte>>>();
    }


}
