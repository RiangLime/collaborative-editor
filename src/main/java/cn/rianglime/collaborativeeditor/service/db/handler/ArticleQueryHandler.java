package cn.rianglime.collaborativeeditor.service.db.handler;

import cn.rianglime.collaborativeeditor.module.operation.Operation;
import org.springframework.stereotype.Component;

/**
 * @ClassName: ArticleQueryHandler
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/2/29 17:18
 */
@Component
public class ArticleQueryHandler extends ArticleHandler{
    @Override
    public void handle(Integer articleId, Operation operation) {

    }

    @Override
    public String query(Integer articleId) {
        return null;
    }
}
