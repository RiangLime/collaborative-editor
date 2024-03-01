package cn.rianglime.collaborativeeditor.websocket.biz;

import cn.rianglime.collaborativeeditor.common.ApplicationContextHolder;
import cn.rianglime.collaborativeeditor.module.operation.Operation;
import cn.rianglime.collaborativeeditor.service.db.biz.RealTimeCheckService;
import cn.rianglime.collaborativeeditor.service.db.handler.ArticleHandler;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName: ArticleGroup
 * @Description: one instance per article
 *               storing user channels and article consumer
 * @Author: Lime
 * @Date: 2024/2/29 16:17
 */
public class ArticleGroup {

    private final Integer articleId;
    private final Set<Channel> connectorChannels;
    private final Map<Integer,Channel> userChannelMap;
    private final Lock lock;
    private final Queue<Operation> operationWaitingList;
    private static final Integer DEFAULT_CONSUMER_PERIOD = 500;
    private final ArticleOperationConsumer consumer;
    private boolean consumerStart = false;
    private Timer timer;

    public ArticleGroup(Integer articleId) {
        this.articleId = articleId;
        connectorChannels = new HashSet<>();
        lock = new ReentrantLock();
        userChannelMap = new HashMap<>();
        operationWaitingList = new ConcurrentLinkedQueue<>();
        consumer = new ArticleOperationConsumer(articleId,DEFAULT_CONSUMER_PERIOD);
        // additional timer
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                broadcastData();
            }
        },0,5000);
    }

    /**
     * get article data
     * @return article data
     */
    private String getData(){
        try {
            lock.lock();
            return ArticleHandler.getHandler(true).query(articleId);
        }finally {
            lock.unlock();
        }
    }

    /**
     * send article data to target user
     * @param channel target user channel
     */
    public void toTargetData(Channel channel){
        String data = getData();
        channel.writeAndFlush(data);
    }

    /**
     * broadcast delta operation
     * @param operation operation
     */
    public void broadcastOperation(Operation operation){
        broadcast(operation);
    }

    /**
     * broadcast article data to all connected user
     */
    public void broadcastData(){
        String data = getData();
        System.out.println("广播"+data);
        broadcast(data);
    }

    /**
     * broadcast realtime article check to all connected user
     */
    public void broadcastRealtimeCheck(){
        String data = getData();
        broadcast(ApplicationContextHolder.getContext().getBean(RealTimeCheckService.class).doCheck(data));
    }

    /**
     * broadcast object
     * @param o object
     */
    public void broadcast(Object o){
        try {
            lock.lock();
            for (Channel channel : connectorChannels){
                channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(o)));
            }
        }finally {
            lock.unlock();
        }
    }

    public Integer getArticleId() {
        return articleId;
    }

    /**
     * get single operation from queue
     * @return operation
     */
    public Operation pollOperation(){
        try {
            lock.lock();
            return operationWaitingList.poll();
        }finally {
            lock.unlock();
        }
    }

    /**
     * user connect
     * @param userId userId
     * @param channel user channel
     */
    public void addConnector(Integer userId, Channel channel){
        //todo user map
        connectorChannels.add(channel);
    }

    /**
     * user disconnect
     * @param channel user channel
     */
    public void removeConnector(Channel channel){
        connectorChannels.remove(channel);
    }

    public boolean containConnector(Channel channel){
        return connectorChannels.contains(channel);
    }

    /**
     * get connected user number
     * @return connected user number
     */
    public int getConnectorNumber(){
        return connectorChannels.size();
    }

    public void addQueue(Operation operation){
        operationWaitingList.add(operation);
    }

    public void startConsumer(){
        consumerStart = true;
        consumer.startConsume();
    }

    public void stopConsumer(){
        consumerStart = false;
        consumer.stopConsume();
    }

    public void stopBroadcast(){
        timer.cancel();
        timer = null;
    }

    public boolean isConsumerStart() {
        return consumerStart;
    }


}
