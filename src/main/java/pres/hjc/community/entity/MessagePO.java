package pres.hjc.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/4  19:22
 * @description :
 */
@Data
public class MessagePO {

    private int id;
    private int fromId;
    private int toId;
    private String conversationId;
    private String content;
    private int status;
    private Date createTime;
}
