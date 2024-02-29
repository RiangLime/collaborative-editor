package cn.rianglime.collaborativeeditor.module.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户第三方登录授权表
 * @TableName Article
 */
@TableName(value ="Article")
@Data
public class Article implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * data
     */
    @TableField(value = "content")
    private String content;

    /**
     * v
     */
    @TableField(value = "version")
    private Integer version;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_created")
    private Date gmtCreated;

    /**
     * 更新时间
     */
    @TableField(value = "gmt_modified")
    private Date gmtModified;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}