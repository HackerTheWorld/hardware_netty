package nettyNIO.hander.socket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import nettyNIO.hander.DecoderMap;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义编码器
 */
public class SocketDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out) throws Exception {
        //处理丢包粘包
        ArrayList<ArrayList<Byte>> bytes = null ;
        if(DecoderMap.decoderMap.containsKey(channelHandlerContext.channel().id())){
            bytes = DecoderMap.decoderMap.get(channelHandlerContext.channel().id());
        }

        int low = 0;
        if(bytes == null){
            bytes = new ArrayList<ArrayList<Byte>>();
        }
        //读取本次请求字节码
        for (int i = 0; i < byteBuf.readableBytes();i++){
             Byte by = byteBuf.readByte();
             //分割的字符串的ascii
             if(by == 47){
                low ++;
             }else {
                 if(low >= bytes.size() || bytes.get(low).isEmpty()){
                     ArrayList<Byte> byteElem = new ArrayList<Byte>();
                     bytes.add(byteElem);
                 }
                 bytes.get(low).add(by);
             }
        }


        for (int i = 0 ; i < low; i++){
            Object[] byteStr = bytes.get(i).toArray();
            byte[] bytess = new byte[byteStr.length];
            for (int j = 0;j < byteStr.length;j++){
                Byte byt = (Byte)byteStr[j];
                bytess[j] = byt.byteValue();
            }
            out.add(new String(bytess));
        }
        for (int i = 0;i < low;i++){
            bytes.remove(i);
        }
        if(bytes.isEmpty()){
            DecoderMap.decoderMap.remove(channelHandlerContext.channel().id());
        }else {
            DecoderMap.decoderMap.put(channelHandlerContext.channel().id(),bytes);
        }
    }
}
