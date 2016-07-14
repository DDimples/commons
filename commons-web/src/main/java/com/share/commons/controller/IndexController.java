package com.share.commons.controller;

import com.share.commons.cache.util.CacheTool;
import com.share.commons.util.StringUtil;
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

    private String regin = "commons";

    @RequestMapping(value = "/index")
    @ResponseBody
    public Object index(HttpServletRequest request){
        return "index";
    }

    @RequestMapping(value = "/cache")
    @ResponseBody
    public Object cache(HttpServletRequest request){
        String cacheKey = request.getParameter("key");
        if(StringUtil.isEmpty(cacheKey)){
            return "key can't be null ";
        }
        String configkey = request.getParameter("configkey")==null?"test":request.getParameter("configkey");
        String value = request.getParameter("value")==null?"default":request.getParameter("value");
        Object cache = CacheTool.getObjectFromCache(regin,configkey,cacheKey);
        if(cache!=null){
            return cache;
        }else {
            CacheTool.setObject2Cache(regin,configkey,cacheKey,value);
            Object res = CacheTool.getObjectFromCache(regin,configkey,cacheKey);
            System.out.println(res);
            return res;
        }
    }

}
