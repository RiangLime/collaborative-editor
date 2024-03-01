package cn.rianglime.collaborativeeditor.service.db.biz;

import com.alibaba.fastjson.JSON;
import com.github.houbb.word.checker.util.WordCheckerHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: RealTimeCheckService
 * @Description: realtime language tool checker
 * @Author: Lime
 * @Date: 2024/3/1 11:13
 */
@Service
public class RealTimeCheckService {

    /**
     * check language problems of data
     * @param data data to be checked
     * @return the data structure to be determined
     */
    public String doCheck(String data){
        Map<String, List<String>> checker = WordCheckerHelper.correctMap(data,1);
        return JSON.toJSONString(checker);
    }

}
