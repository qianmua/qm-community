package pres.hjc.community.vo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
@Accessors( chain = true)
public class EventVO {
    private String topic;
    private Integer userId;
    private Integer entityType;
    private Integer entityId;
    private Integer entityUserId;

//    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Map<String,Object> data = new HashMap<>();


    public EventVO setData(String key , Object value) {
        this.data.put(key , value);
        return this;
    }
}
