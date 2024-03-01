package cn.rianglime.collaborativeeditor.module.operation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: Operation
 * @Description: atom operation of article
 * @Author: Lime
 * @Date: 2024/2/29 15:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Operation implements Serializable {
    private String operationId;
    /**
     * 1 add 2 del
     */
    private Integer operationType;
    /**
     * 在第i个字母后操作 从0开始计数
     * abc -> avbc position=1
     */
    private Integer position;
    private String content;
    private Integer gCounter;
    private String timestamp;
}
