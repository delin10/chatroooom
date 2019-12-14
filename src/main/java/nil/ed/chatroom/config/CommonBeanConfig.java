package nil.ed.chatroom.config;

import nil.ed.chatroom.util.sensitive.ForwardMatchSensitiveFilter;
import nil.ed.chatroom.util.sensitive.SensitiveFilter;
import nil.ed.chatroom.util.sensitive.matcher.AbstractLevelMatcher;
import nil.ed.chatroom.util.sensitive.matcher.TierTreeMatcher;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author delin10
 * @since 2019/10/15
 **/
@Configuration
public class CommonBeanConfig {
    @Bean
    public Executor databaseQueryExecutor(){
        ThreadFactory threadFactory = runnable ->{
            return new Thread(runnable, "database-query-thread");
        };
        return new ThreadPoolExecutor(4, 4, 100,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(100), threadFactory);
    }

    @Bean
    public SensitiveFilter sensitiveFilter() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("sensitive-words.txt");
        List<String> dict = FileUtils.readLines(classPathResource.getFile(), StandardCharsets.UTF_8);
        return new ForwardMatchSensitiveFilter(new TierTreeMatcher(AbstractLevelMatcher.MatchLevel.NORMAL, dict));
    }
}
