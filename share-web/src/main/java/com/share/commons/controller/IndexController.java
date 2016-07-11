package com.share.commons.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 程祥 on 16/7/9.
 * Function：
 */
@Controller
public class IndexController {

    @RequestMapping(value = "/index")
    @ResponseBody
    public Object index(HttpServletRequest request){
        return "index";
    }

}
