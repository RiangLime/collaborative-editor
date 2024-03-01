package cn.rianglime.collaborativeeditor.websocket;

import cn.rianglime.collaborativeeditor.websocket.biz.ArticleGroup;
import cn.rianglime.collaborativeeditor.websocket.biz.ArticleGroupCenter;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Map;

/**
 * @ClassName: EditorServerMessageHandler
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/2/29 15:10
 */
@Slf4j
public class EditorServerMessageHandler extends SimpleChannelInboundHandler<WebSocketFrame> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        if (msg instanceof TextWebSocketFrame) {
            String message = ((TextWebSocketFrame) msg).text();
            System.out.println("服务器端收到消息[text]:" + message);
            // 进行分组操作 添加到Connector
            WebSocketMessage webSocketMessage = JSON.parseObject(message, WebSocketMessage.class);

            if (!ArticleGroupCenter.ARTICLE_GROUP_MAP.containsKey(webSocketMessage.getArticleId())) {
                ArticleGroupCenter.ARTICLE_GROUP_MAP.put(webSocketMessage.getArticleId(), new ArticleGroup(webSocketMessage.getArticleId()));
            }
            ArticleGroupCenter.ARTICLE_GROUP_MAP.get(webSocketMessage.getArticleId()).addConnector(webSocketMessage.getUserId(), ctx.channel());

            ArticleGroup articleGroup = ArticleGroupCenter.ARTICLE_GROUP_MAP.get(webSocketMessage.getArticleId());
            if (!articleGroup.isConsumerStart()){
                articleGroup.startConsumer();
            }
            // 业务操作
            // case1 查询
            if (webSocketMessage.getIsQuery()) {
                articleGroup.toTargetData(ctx.channel());
            }
            // case2 operation
            if (!webSocketMessage.getIsQuery() && ObjectUtils.isNotEmpty(webSocketMessage.getOperation())) {
                articleGroup.addQueue(webSocketMessage.getOperation());
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与客户端建立连接，通道开启！");
        ctx.writeAndFlush("与客户端建立连接，通道开启！");

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与客户端断开连接，通道关闭！");
        for (Map.Entry<Integer, ArticleGroup> entry : ArticleGroupCenter.ARTICLE_GROUP_MAP.entrySet()) {
            // 找到对应的 组 并删除connector
            if (entry.getValue().containConnector(ctx.channel())) {
                entry.getValue().removeConnector(ctx.channel());
                if (entry.getValue().getConnectorNumber() == 0) {
                    ArticleGroupCenter.stopGroup(entry.getKey());
                }
                return;
            }
        }
    }

}

