package com.mkst.umap.app.admin.api.bean.result.arraign;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName ScheduleResult
 * @Description 获取专案预约时已占用的时间点的排班
 * @Author wangyong
 * @Date 2020-07-06 16:19
 */
@Data
public class ScheduleResult {

    /**
     * 房间Id
     */
    private String roomId;

    /**
     * 房间名
     */
    private String roomName;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

}
