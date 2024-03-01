package cn.rianglime.collaborativeeditor;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static org.springframework.boot.SpringApplication.*;

@SpringBootApplication
@Slf4j
@MapperScan("cn.rianglime.collaborativeeditor.mapper")
public class CollaborativeEditorApplication {

    public static void main(String[] args) {
        run(CollaborativeEditorApplication.class, args);
        log.info("EDITOR DEMO START FINISHED");
    }

}
