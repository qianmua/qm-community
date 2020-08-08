package pres.hjc.community.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pres.hjc.community.service.DataService;

import java.util.Date;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/8  18:22
 * @description :
 */
@Controller
public class DataController {

    @Autowired
    private DataService dataService;

    @RequestMapping( path = "/data" , method = {RequestMethod.GET , RequestMethod.POST})
    public String getDataView(){
        return "site/admin/data";
    }

    /**
     * 统计 uv
     * 日活跃
     * @param start start
     * @param end end
     * @param model model
     * @return views
     */
    @PostMapping("/data/{type}")
    public String getUV(@PathVariable String type,
                        @DateTimeFormat(pattern = "yyyy-MM-dd") Date start ,
                        @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
                        Model model){

        if ("uv".equals(type)){
            model.addAttribute("uvResult" , dataService.calculateUV(start, end));
        }
        if ("dau".equals(type)){
            model.addAttribute("dauResult" , dataService.caclculateDAU(start, end));
        }

        model.addAttribute("uvStartDate" , start);
        model.addAttribute("uvEndDate" , end);

        //forward:/data
//        return "site/admin/data";
        // 还是同一个 请求
        // forward

        // redirect
        return "forward:/data";
    }

    /**
     * 统计 日活跃
     * @param start
     * @param end
     * @param model
     * @return
     */
    /*@PostMapping("/data/dau")
    public String getDAU(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start ,
                        @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
                        Model model){

        model.addAttribute("dauResult" , dataService.caclculateDAU(start, end));
        model.addAttribute("dauStartDate" , start);
        model.addAttribute("dauEndDate" , end);

        return "forward:/data";
    }*/
}
