package nettyNIO.hander.common;

import io.netty.buffer.ByteBuf;

public class ReleaseUtil {
    
    /***
     * ByteBuf内存释放
     * @param byteBuf 字节码
     */
    public static void releaseByteBuf(ByteBuf byteBuf){
        if (byteBuf.refCnt() != 0){
            byteBuf.release(byteBuf.refCnt());
        }
    }

}
