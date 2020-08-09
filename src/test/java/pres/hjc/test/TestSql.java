package pres.hjc.test;

import org.apache.ibatis.jdbc.SQL;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/8  17:29
 * @description :
 */
public class TestSql {

    public static void main(String[] args) {
        SQL sql = new SQL();
        sql.INSERT_INTO("table_Name")
                .INTO_VALUES("c1" , "c1")
                .INTO_VALUES("c2" , "c2")
                .INTO_VALUES("c3" , "c3")
                .INTO_VALUES("c4" ,"c4")
        .ADD_ROW()
        .INTO_VALUES("a1" ,"a1")
        .INTO_VALUES("a2" ,"a2")
        .INTO_VALUES("a3" ,"a3")
        .INTO_VALUES("a4" ,"a4");


        System.out.println(sql.toString());
    }
}
