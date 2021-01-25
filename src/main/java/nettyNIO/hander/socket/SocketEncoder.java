package nettyNIO.hander.socket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class SocketEncoder extends MessageToByteEncoder<String> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String s, ByteBuf byteBuf) throws Exception {
        byte[] b = s.getBytes();
        int length = b.length;

        //write length of msg
        byteBuf.writeInt(length);

        //write msg
        byteBuf.writeBytes(b);
    }

}
