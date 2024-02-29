package cn.rianglime.collaborativeeditor.websocket.biz;

import cn.rianglime.collaborativeeditor.module.operation.Operation;
import io.netty.channel.Channel;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName: ArticleGroup
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/2/29 16:17
 */
public class ArticleGroup {

    private final Integer articleId;
    private Set<Channel> connectorChannels;
    private Map<Integer,Channel> userChannelMap;
    private final Lock lock;
    private Queue<Operation> operationWaitingList;
    private static final Integer DEFAULT_CONSUMER_PERIOD = 500;
    private ArticleOperationConsumer consumer;

    public ArticleGroup(Integer articleId) {
        this.articleId = articleId;
        connectorChannels = new HashSet<>();
        lock = new ReentrantLock();
        userChannelMap = new HashMap<>();
        operationWaitingList = new ConcurrentLinkedQueue<>();
        consumer = new ArticleOperationConsumer(articleId,DEFAULT_CONSUMER_PERIOD);
        consumer.startConsume();
    }

    /**
     * 对所有连接用户广播 delta操作
     * @param operation delta操作
     */
    public void broadcastOperation(Operation operation){
        broadcast(operation);
    }

    /**
     * 对所有连接用户广播 全文信息
     * @param data
     */
    public void broadcastData(String data){
        broadcast(data);
    }

    /**
     * 对所有连接用户广播 实时检测信息
     */
    public void broadcastRealtimeCheck(){
        broadcast(null);
    }

    public void broadcast(Object o){
        try {
            lock.lock();
            for (Channel channel : connectorChannels){
                channel.writeAndFlush(o);
            }
        }finally {
            lock.unlock();
        }
    }

    public Integer getArticleId() {
        return articleId;
    }

    public Operation poll(){
        try {
            lock.lock();
            return operationWaitingList.poll();
        }finally {
            lock.unlock();
        }
    }
}
