package cn.rianglime.collaborativeeditor.websocket.biz;

import cn.rianglime.collaborativeeditor.module.operation.Operation;
import cn.rianglime.collaborativeeditor.service.db.handler.ArticleHandler;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @ClassName: ArticleOperationConsumer
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/2/29 16:37
 */
public class ArticleOperationConsumer {

    private final Integer articleId;
    private final Integer period;
    private final Timer timer;

    public ArticleOperationConsumer(Integer articleId, Integer period) {
        this.articleId = articleId;
        this.period = period;
        timer = new Timer();
    }

    public void startConsume() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    //do Something
                    consume();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, period);
    }

    public void stopConsume() {
        timer.cancel();
    }

    private void consume() {
        Operation operation = ArticleGroupCenter.ARTICLE_GROUP_MAP.get(articleId).poll();
        ArticleHandler.getHandler(operation,false).handle(articleId, operation);
    }


}
