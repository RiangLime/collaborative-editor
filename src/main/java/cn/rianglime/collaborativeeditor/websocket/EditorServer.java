package cn.rianglime.collaborativeeditor.websocket;

import cn.rianglime.collaborativeeditor.common.ApplicationContextHolder;
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
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: EditorServer
 * @Description: server of websocket [ dealing editor
 * @Author: Lime
 * @Date: 2024/2/29 15:07
 */
@Service
@Slf4j
public class EditorServer implements InitializingBean {

    /**
     * port of websocket
     */
    @Value("${server.editor-port}")
    private Integer webSocketServerPort;
    @Resource
    private ConfigurableApplicationContext context;

    /**
     * ThreadPool to run websocket server
     */
    private static final ExecutorService executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());

    @Override
    public void afterPropertiesSet() throws Exception {
        executorService.execute(new Runnable() {
            // start server
            @Override
            public void run() {
                try {
                    /*
                     * init server
                     * this method mill be blocked
                     * so run a new runnable
                     */
                    startServer();
                }catch (Exception e){
                    log.error("editor server start failed");
                    context.close();
                }
            }
        });
    }

    /**
     * start netty websocket server
     * @throws InterruptedException e
     */
    private void startServer() throws InterruptedException {
        // create boss & worker thread pool
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(8);
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    // logging handler
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // http protocol coder
                            pipeline.addLast(new HttpServerCodec());
                            // write as chunk chunkedWriter
                            pipeline.addLast(new ChunkedWriteHandler());
                            /*
                             * HttpObjectAggregator can gather multi parts together
                             * data will be divided into fragments when  big enough
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            pipeline.addLast(new WebSocketServerProtocolHandler("/editor",null,true));
                            // biz handler
                            pipeline.addLast(new EditorServerMessageHandler());
                        }
                    });
            // start
            ChannelFuture channelFuture = serverBootstrap.bind(webSocketServerPort).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully().sync();
            workerGroup.shutdownGracefully().sync();
        }
    }
}
