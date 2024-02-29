package cn.rianglime.collaborativeeditor.service.db.handler;

import cn.rianglime.collaborativeeditor.common.ApplicationContextHolder;
import cn.rianglime.collaborativeeditor.module.operation.Operation;

/**
 * @ClassName: ArticleHandler
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/2/29 14:55
 */
public abstract class ArticleHandler {
    public abstract void handle(Integer articleId, Operation operation);

    public abstract String query(Integer articleId);

    public static ArticleHandler getHandler(Operation operation, boolean isQuery) {


        return isQuery ?
                ApplicationContextHolder.getContext().getBean(ArticleQueryHandler.class) :
                switch (operation.getOperationType()) {
            case 1 -> ApplicationContextHolder.getContext().getBean(ArticleAddHandler.class);
            case 2 -> ApplicationContextHolder.getContext().getBean(ArticleDeleteHandler.class);
            default -> throw new RuntimeException("unknown operation");
        };
    }

}
