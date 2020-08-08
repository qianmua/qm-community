package pres.hjc.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import pres.hjc.community.tools.GenRedisKeyUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/8  17:21
 * @description :
 */
@Service
public class DataService {

    @Autowired
    private RedisTemplate redisTemplate;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");


    /**
     * 计入UV
     * @param ip ip
     */
    public void recordUV(String ip){
        String vkey = GenRedisKeyUtil.getUVKey(sdf.format(new Date()));
        redisTemplate.opsForHyperLogLog().add(vkey , ip);
    }

    /**
     * 统计 日期之内UV
     * @param s
     * @param l
     * @return
     */
    public long calculateUV(Date s , Date l){
        if (s == null || l == null){
            throw new IllegalArgumentException("参数空 null");
        }

        // 整理key
        // 日期范围
        ArrayList<String> strings = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(s);
        // getTime 比 l 小
        while (!calendar.getTime().after(l)) {
            strings.add(GenRedisKeyUtil.getUVKey(sdf.format(calendar.getTime())));

            calendar.add(Calendar.DATE , 1);
        }

        // 合并日期
        String uVkey = GenRedisKeyUtil.getUVKey(sdf.format(s) ,  sdf.format(l));


        // 记录
        redisTemplate.opsForHyperLogLog().union(uVkey, strings.toArray());

        // 统计结果
        return redisTemplate.opsForHyperLogLog().size(uVkey);

    }


    /**
     * 统计 用户
     * @param userId
     */
    public void recordDAU(int userId){
        String dauKey = GenRedisKeyUtil.getDAUKey(sdf.format(new Date()));
        redisTemplate.opsForValue().setBit(dauKey , userId , true);
    }


    /**
     * 范围内 的 活跃 用户
     * all
     * 只要 范围内 活跃一次 就算
     *
     * @param s
     * @param l
     * @return
     */
    public long caclculateDAU(Date s , Date l){
        if (s == null || l == null){
            throw new IllegalArgumentException("参数空 null");
        }
        // 整理key
        // 日期范围
        ArrayList<byte[]> dau = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(s);
        // getTime 比 l 小
        while (!calendar.getTime().after(l)) {
            dau.add( GenRedisKeyUtil.getDAUKey(sdf.format(calendar.getTime())).getBytes() );

            calendar.add(Calendar.DATE , 1);
        }

        // or 运算
        return (long) redisTemplate.execute((RedisCallback) conn -> {
            String dauKey = GenRedisKeyUtil.getDAUKey(sdf.format(s), sdf.format(l));

            // 操作位图
            conn.bitOp(RedisStringCommands.BitOperation.OR ,
                    dauKey.getBytes() ,
                    dau.toArray( new byte[0][0]));

            // 统计
            return conn.bitCount(dauKey.getBytes());
        });

    }

}
