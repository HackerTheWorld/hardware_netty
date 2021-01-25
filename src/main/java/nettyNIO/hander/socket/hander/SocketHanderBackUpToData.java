package nettyNIO.hander.socket.hander;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import nettyNIO.hander.common.HbaseUtil;

public class SocketHanderBackUpToData extends ChannelInboundHandlerAdapter {

    private HbaseUtil hbaseUtil = new HbaseUtil("");
    private final String TABLE_NAME = "t_machine";
    private final String COLUMN_FAMILY = "machine";

    /**
     * 接收过滤之后的数据
     */
    @Override
    @SuppressWarnings("unchecked")
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        
        if(msg instanceof JSONObject){
            //存入Hbase数据库备份
            Map<String, Object> map = (Map<String, Object>)msg;
            List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
            list.add(map);
            hbaseUtil.insertList(TABLE_NAME, COLUMN_FAMILY, list);
        }

    }

}
