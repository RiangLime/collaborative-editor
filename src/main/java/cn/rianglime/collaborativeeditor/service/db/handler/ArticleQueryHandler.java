package cn.rianglime.collaborativeeditor.service.db.handler;

import cn.rianglime.collaborativeeditor.module.operation.Operation;
import cn.rianglime.collaborativeeditor.service.db.interfaces.ArticleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @ClassName: ArticleQueryHandler
 * @Description: query db article
 * @Author: Lime
 * @Date: 2024/2/29 17:18
 */
@Component
public class ArticleQueryHandler extends ArticleHandler{

    @Resource
    private ArticleService articleService;

    @Override
    public void handle(Integer articleId, Operation operation) {

    }

    /**
     * query with mybatis plus
     * @param articleId article id
     * @return article content
     */
    @Override
    public String query(Integer articleId) {
        return articleService.getById(articleId).getContent();
    }
}
