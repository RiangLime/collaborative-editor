package cn.rianglime.collaborativeeditor.websocket;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @ClassName: EditorServerMessageHandler
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/2/29 15:10
 */
@Slf4j
public class EditorServerMessageHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    // 读取客户端发送的请求报文
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器端收到消息 = " + msg.text());
        WebSocketMessage webSocketMessage = JSON.parseObject(msg.text(),WebSocketMessage.class);
        // 进行分组操作

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与客户端建立连接，通道开启！");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

    }

}

