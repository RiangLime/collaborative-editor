package cn.rianglime.collaborativeeditor.module.operation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: Operation
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/2/29 15:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Operation implements Serializable {
    /**
     * 1新增 2删除
     */
    private Integer operationType;
    private Integer position;
    private String content;
}
