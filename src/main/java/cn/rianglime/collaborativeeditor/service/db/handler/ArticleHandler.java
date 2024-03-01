package cn.rianglime.collaborativeeditor.service.db.handler;

import cn.rianglime.collaborativeeditor.common.ApplicationContextHolder;
import cn.rianglime.collaborativeeditor.module.operation.Operation;

/**
 * @ClassName: ArticleHandler
 * @Description: base article handler [map operations to db
 * @Author: Lime
 * @Date: 2024/2/29 14:55
 */
public abstract class ArticleHandler {
    public abstract void handle(Integer articleId, Operation operation);

    public abstract String query(Integer articleId);

    public static ArticleHandler getHandler(boolean isQuery) {
        return isQuery ?
                ApplicationContextHolder.getContext().getBean(ArticleQueryHandler.class) :
                ApplicationContextHolder.getContext().getBean(ArticleDeltaHandler.class);
    }

}
