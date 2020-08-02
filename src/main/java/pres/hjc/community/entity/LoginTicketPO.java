package pres.hjc.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/2  18:13
 * @description :
 */
@Data
public class LoginTicketPO {

    private int id;
    private int userId;
    private String ticket;
    private int status;
    private Date expired;


}
