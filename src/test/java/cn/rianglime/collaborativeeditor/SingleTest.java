package cn.rianglime.collaborativeeditor;

import cn.rianglime.collaborativeeditor.module.operation.Operation;
import cn.rianglime.collaborativeeditor.websocket.WebSocketMessage;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;

/**
 * @ClassName: SingleTest
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/3/1 11:58
 */
public class SingleTest {

    @Test
    public void testPosition(){
        Operation addOperation = new Operation();
        addOperation.setOperationType(1);
        addOperation.setPosition(100);
        addOperation.setContent("insert ");
        String article = "hello how are you?";
        String newData = article.substring(0,addOperation.getPosition()) + addOperation.getContent()
                + article.substring(addOperation.getPosition());
        System.out.println(newData);
    }

    @Test
    public void testJson(){
        String data = "{ \"articleId\":1, \"isQuery\":false, \"operation\":{ \"operationType\":1, \"position\":1, \"content\":\"hello\" } }";
        WebSocketMessage bean = JSON.parseObject(data,WebSocketMessage.class);
        System.out.println(bean.getArticleId());
    }

}
