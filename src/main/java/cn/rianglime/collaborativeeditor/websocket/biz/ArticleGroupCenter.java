package cn.rianglime.collaborativeeditor.websocket.biz;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: ArticleGroupCenter
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/2/29 16:27
 */
public class ArticleGroupCenter {

    public static final Map<Integer,ArticleGroup> ARTICLE_GROUP_MAP = new HashMap<>();
    public static void stopGroup(int articleId){
        ArticleGroup articleGroup = ARTICLE_GROUP_MAP.get(articleId);
        articleGroup.stopConsumer();
        articleGroup.stopBroadcast();
        // 置空 析构
        articleGroup = null;
    }

}
