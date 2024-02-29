package cn.rianglime.collaborativeeditor.service.db.handler;

import cn.rianglime.collaborativeeditor.module.operation.Operation;
import org.springframework.stereotype.Component;

/**
 * @ClassName: ArticleAddHandler
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/2/29 14:55
 */
@Component
public class ArticleAddHandler extends ArticleHandler{

    @Override
    public String query(Integer articleId) {
        return null;
    }

    @Override
    public void handle(Integer articleId, Operation operation) {

    }
}
