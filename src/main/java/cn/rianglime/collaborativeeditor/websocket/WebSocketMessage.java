package cn.rianglime.collaborativeeditor.websocket;

import cn.rianglime.collaborativeeditor.module.operation.Operation;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: WebSocketMessage
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/2/29 15:25
 */
@Data
public class WebSocketMessage implements Serializable {
    private Integer articleId;
    private Integer userId;
    private Boolean isQuery = false;
    private Operation operation;
}
