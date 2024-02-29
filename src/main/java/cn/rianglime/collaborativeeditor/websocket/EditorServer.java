package cn.rianglime.collaborativeeditor.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @ClassName: EditorServer
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/2/29 15:07
 */
@Service
public class EditorServer implements InitializingBean {

//    private Map<Integer>

    @Override
    public void afterPropertiesSet() throws Exception {
        // 创建线程池执行器
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(8);
        try {
            // 服务器启动引导对象
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler(LogLevel.INFO)) // 为 bossGroup 添加 日志处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 因为使用http协议，所以需要使用http的编码器，解码器
                            pipeline.addLast(new HttpServerCodec());
                            // 以块方式写，添加 chunkedWriter 处理器
                            pipeline.addLast(new ChunkedWriteHandler());
                            /**
                             * 说明：
                             *  1. http数据在传输过程中是分段的，HttpObjectAggregator可以把多个段聚合起来；
                             *  2. 这就是为什么当浏览器发送大量数据时，就会发出多次 http请求的原因
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            /**
                             * 说明：
                             *  1. 对于 WebSocket，它的数据是以帧frame 的形式传递的；
                             *  2. 可以看到 WebSocketFrame 下面有6个子类
                             *  3. 浏览器发送请求时： ws://localhost:7000/hello 表示请求的uri
                             *  4. WebSocketServerProtocolHandler 核心功能是把 http协议升级为 ws 协议，保持长连接；
                             *      是通过一个状态码 101 来切换的
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
                            // 自定义handler ，处理业务逻辑
                            pipeline.addLast(new EditorServerMessageHandler());
                        }
                    });
            // 启动服务器，监听端口，阻塞直到启动成功
            ChannelFuture channelFuture = serverBootstrap.bind(8089).sync();
            // 阻塞直到channel关闭
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully().sync();
            workerGroup.shutdownGracefully().sync();
        }

    }
}
