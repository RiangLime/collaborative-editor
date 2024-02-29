package cn.rianglime.collaborativeeditor.service.db.impl;

import cn.rianglime.collaborativeeditor.service.db.interfaces.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.rianglime.collaborativeeditor.module.entity.Article;
import cn.rianglime.collaborativeeditor.mapper.ArticleMapper;
import org.springframework.stereotype.Service;

/**
* @author riang
* @description 针对表【Article(用户第三方登录授权表)】的数据库操作Service实现
* @createDate 2024-02-29 14:51:36
*/
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService {

}




