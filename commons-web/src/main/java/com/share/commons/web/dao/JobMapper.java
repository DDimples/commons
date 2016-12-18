package com.share.commons.web.dao;

import com.share.commons.web.model.quartz.TriggerInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 程祥 on 16/10/20.
 * Function：
 */
@Repository
public interface JobMapper {

    List<TriggerInfo> getTriggersByJobGroup(String jobGroup);

    List<TriggerInfo> getAllTriggers();

    List<TriggerInfo> searchTriggerByNameOrGroup(String queryString);
}
