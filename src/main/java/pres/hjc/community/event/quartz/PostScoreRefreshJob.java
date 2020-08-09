package pres.hjc.community.event.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import pres.hjc.community.service.DiscussPostService;
import pres.hjc.community.service.LikeService;
import pres.hjc.community.tools.CommunityStatusCode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/9  19:24
 * @description :
 */
@Slf4j
public class PostScoreRefreshJob implements Job , CommunityStatusCode {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private LikeService likeService;

    /**
     * 源日期
     */
    public static final Date epoch;

    static {
        try {
            epoch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2020-08-08 00:00:00");
        } catch (ParseException e) {
            throw new RuntimeException("初始化元日期异常",e);
        }
    }

    /**
     * 调度中心
     * @param jobExecutionContext jobExecutionContext
     * @throws JobExecutionException err
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }
}
