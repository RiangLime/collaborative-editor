package cn.rianglime.collaborativeeditor.service.db.handler;

import cn.rianglime.collaborativeeditor.module.entity.Article;
import cn.rianglime.collaborativeeditor.module.operation.Operation;
import cn.rianglime.collaborativeeditor.service.db.interfaces.ArticleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @ClassName: ArticleDeltaHandler
 * @Description: operations -> db
 * @Author: Lime
 * @Date: 2024/2/29 14:55
 */
@Component
public class ArticleDeltaHandler extends ArticleHandler {

    @Resource
    private ArticleService articleService;

    @Override
    public String query(Integer articleId) {
        return null;
    }

    /**
     * map operations to db
     * @param articleId articleId
     * @param operation single operation
     */
    @Override
    public void handle(Integer articleId, Operation operation) {
        handleArticle(articleId, operation);
        handleSuggestion(articleId, operation);
    }

    /**
     * map operations to article table
     * @param articleId articleId
     * @param operation single operation
     */
    private void handleArticle(Integer articleId, Operation operation) {
        Article article = articleService.getById(articleId);
        switch (operation.getOperationType()) {
            case 1 -> handleArticleOnOperationAdd(article, operation);
            case 2 -> handleArticleOnOperationDelete(article, operation);
            default -> {}
        }
    }

    /**
     * map add operation to article table
     * @param article article id
     * @param operation single operation
     */
    private void handleArticleOnOperationAdd(Article article, Operation operation) {
        String newData = article.getContent().substring(0, operation.getPosition()) + operation.getContent()
                + article.getContent().substring(operation.getPosition());
        articleService.lambdaUpdate().eq(Article::getId, article.getId()).set(Article::getContent, newData).update();
    }

    /**
     * map delete operation to article table
     * @param article article id
     * @param operation single operation
     */
    private void handleArticleOnOperationDelete(Article article, Operation operation) {
        String front = article.getContent().substring(0, operation.getPosition());
        String middle = article.getContent().substring(operation.getPosition(), operation.getPosition() + operation.getContent().length());
        String after = article.getContent().substring(operation.getPosition() + operation.getContent().length());
        if (middle.equals(operation.getContent())) {
            String newData = front + after;
            articleService.lambdaUpdate().eq(Article::getId, article.getId()).set(Article::getContent, newData).update();
        }
    }

    /**
     * map operations to suggestion table
     * @param articleId article id
     * @param operation single operation
     */
    private void handleSuggestion(Integer articleId, Operation operation) {

    }

}
