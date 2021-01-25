/* ====================================================================================================
 * Project Name     [test_technology]
 * File Name        [nettyNIO.hander.websocket.WebSocketWorkHander.java]
 * Creation Date    [2021-01-12]
 *
 * Copyright© 2021 瑞声科技[AAC Technologies Holdings] All Rights Reserved
 *
 * ====================================================================================================
 * Change Log
 * ====================================================================================================
 * 2021-01-12     潘凌云      [Init] .
 * ==================================================================================================== */
package nettyNIO.hander.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.*;
import nettyNIO.hander.AllChannls;

import java.util.Map;

/**
 * <p></p>
 *
 * @author <a href="mailto:panlingyun@aactechnologies.com">潘凌云</a>
 * @version 1.0.0
 * @since jdk 1.8
 */
@SuppressWarnings("unchecked")
public class WebSocketWorkHander extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

            Map<String,Object> msgMap = (Map<String,Object>)msg;
            
            WebSocketFrame webSocketFrame = (WebSocketFrame) msgMap.get("msg");
            WebSocketServerHandshaker handShaker = (WebSocketServerHandshaker)msgMap.get("handShaker");
            handlerWebSocketFrame(ctx, webSocketFrame,handShaker);
            TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame)webSocketFrame;
            //处理websocket客户端的消息
            System.out.println("客户端收到服务器数据:" + textWebSocketFrame.text());
            if(webSocketFrame.refCnt() != 0){
                System.out.println("释放 webSocketFrame");
                webSocketFrame.release(webSocketFrame.refCnt());
            }
            if(textWebSocketFrame.refCnt() != 0){
                System.out.println("释放 textWebSocketFrame");
                textWebSocketFrame.release(textWebSocketFrame.refCnt());
            }
    }

    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame,WebSocketServerHandshaker handShaker){
        // 判断是否关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            handShaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        // 判断是否ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(
                    new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        if (!(frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format(
                    "%s frame types not supported", frame.getClass().getName()));
        }
        // 返回应答消息
        String request = ((TextWebSocketFrame) frame).text();
        TextWebSocketFrame tws = new TextWebSocketFrame("return mess from webserver "+" : "+request);
        // 群发
        for(ChannelHandlerContext cctx: AllChannls.channelMap.values()){
            cctx.writeAndFlush(tws);
        }
        for(ChannelHandlerContext cctx:AllChannls.webChanelMap.values()){
            cctx.writeAndFlush(tws);
        }
        // 返回【谁发的发给谁】
        // ctx.channel().writeAndFlush(tws);
    }

}

