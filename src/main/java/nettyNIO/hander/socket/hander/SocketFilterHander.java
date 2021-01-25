package nettyNIO.hander.socket.hander;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import nettyNIO.hander.AllChannls;
import nettyNIO.hander.common.ReleaseUtil;

@ChannelHandler.Sharable
public class SocketFilterHander extends ChannelInboundHandlerAdapter {

    /**
     * channel 通道 action 活跃的
     *
     * 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
     *
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        AllChannls.channelMap.put(ctx.channel().id(),ctx);
        System.out.println(ctx.channel().localAddress().toString() + " 通道已激活！");
    }

    /**
     * channel 通道 Inactive 不活跃的
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。客户端与服务端的关闭了通信通道并且不可以传输数据
     *
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().localAddress().toString() + " 通道不活跃！");
        // 关闭流
        ctx.channel().close();
    }

    /**
     * 读取服务器发送过来的信息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //解码完成后调用
        if(msg instanceof ByteBuf){
            ByteBuf byteBuf = (ByteBuf)msg;
            String jsonStr = byteBuf.toString(CharsetUtil.UTF_8);
            /** 
             * {
             *   "jsonId":"信息的唯一标识码",
             *   "flag":"用于测试是否是垃圾数据",
             *   "mouldNoSys":"编码",
             *   "num":"统计数据"
             * }
            */
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            System.out.println("客户端收到服务器数据:" + jsonStr);
            
            //返回信息给设备提示数据已成功接收
            Map<String, Object> returnMap = new HashMap<String,Object>();
            returnMap.put("jsonId", jsonObject.getString("jsonId"));
            returnMap.put("sucess", "true"); 
            if(!filter(jsonObject)){
                returnMap.put("flag", "N");
                ctx.fireChannelRead(jsonObject);
            }else{
                returnMap.put("flag", "Y"); 
            }
            JSONObject returnJson = new JSONObject(returnMap);
            ByteBuf buf = Unpooled.buffer();
            buf.capacity(returnJson.toJSONString().getBytes().length+byteBuf.readableBytes());
            buf.writeBytes(returnJson.toJSONString().getBytes(),0,returnJson.toJSONString().getBytes().length);
            buf.retain(2);
            ctx.writeAndFlush(buf);
            ReleaseUtil.releaseByteBuf(buf); 
            ReleaseUtil.releaseByteBuf(byteBuf);
        }
    }

    /**
     * 单个字符解码完成调用
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        
    }

    /**
     * 服务端发生异常的操作
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
        System.out.println("异常信息：\r\n" + cause.getMessage());
    }

    //模拟数据过滤
    private boolean filter(JSONObject json){
        return "Y".equals(json.getString("flag"));
    }

}
