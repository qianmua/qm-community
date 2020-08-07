package pres.hjc.community.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/7  13:50
 * @description :
 */
@Data
public class EventVO {
    private String topic;
    private Integer userId;
    private Integer entityType;
    private Integer entityId;
    private Integer entityUserId;
    private Map<String,Object> data = new HashMap<>();


}
