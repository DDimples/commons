package com.share.commons.web.controller;

import com.share.commons.util.DateUtil;
import com.share.commons.web.dao.JobMapper;
import com.share.commons.web.model.EmployeeModel;
import com.share.commons.web.model.quartz.TriggerInfo;
import com.share.commons.web.model.request.DataTableRequest;
import com.share.commons.web.model.response.DataTableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by 程祥 on 16/10/20.
 * Function：
 */
@Controller
@RequestMapping(value = "/quartz")
public class QuartzController  {

    @ModelAttribute(value = "params")
    public DataTableRequest setParams(HttpServletRequest request){
        DataTableRequest params = new DataTableRequest();
        params.setOrder_column(request.getParameter(DataTableRequest.ORDER_COLUMN));
        params.setOrder_dir(request.getParameter(DataTableRequest.ORDER_DIR));
        params.setSearch_regex(request.getParameter(DataTableRequest.SEARCH_REGEX));
        params.setSearch_value(request.getParameter(DataTableRequest.SEARCH_VALUE));
        params.setColumns_0_data(request.getParameter(DataTableRequest.COLUMNS_0_DATA));
        params.setColumns_0_name(request.getParameter(DataTableRequest.COLUMNS_0_NAME));
        params.setColumns_0_orderable(request.getParameter(DataTableRequest.COLUMNS_0_ORDERABLE));
        params.setColumns_0_searchable(request.getParameter(DataTableRequest.COLUMNS_0_SEARCHABLE));
        return params;
    }


    @RequestMapping(value = "/index")
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView("quartz/index");
        return mv;
    }


    @Autowired
    private JobMapper jobDao;

    /**
     * table ajax 提供数据
     */
    @RequestMapping(value = "/getTableData",method = RequestMethod.POST)
    @ResponseBody
    public Object getTableData(HttpServletRequest request,@ModelAttribute("params") DataTableRequest params){

        DataTableResponse<TriggerInfo> response = new DataTableResponse<TriggerInfo>();
        int draw = Integer.parseInt(request.getParameter("draw"));
        List<TriggerInfo> dataList = jobDao.getAllTriggers();
        TriggerInfo info = new TriggerInfo();
        info.setSchedName("123");
        info.setStartTime(DateUtil.getCurrentMSEL());
        info.setJobName("测试任务名称");
        dataList.add(info);
        response.setData(dataList);
        response.setDraw(draw++);
        response.setRecordsFiltered(0);
        response.setRecordsTotal(0);
        return response;
    }

}
