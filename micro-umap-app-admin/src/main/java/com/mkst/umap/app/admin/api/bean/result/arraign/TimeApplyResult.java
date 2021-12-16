package com.mkst.umap.app.admin.api.bean.result.arraign;

import cn.hutool.db.DaoTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName TimeApplyResult
 * @Description 根据
 * @Author wangyong
 * @Modified By:
 * @Date 2020-10-23 10:59
 */
@Data
@ApiModel("提审预约对象")
public class TimeApplyResult {

    @ApiModelProperty("时间段")
    private String timeCon;
    @ApiModelProperty("流水号")
    private Long id;
    /** 当前时间状态 */
    private String nowStatus;
    private String status;
    private String auditStatus;
    @ApiModelProperty("检察官名称")
    private String useByName;
    @ApiModelProperty("部门")
    private String deptName;
    @ApiModelProperty("提审室")
    private String roomName;
    @ApiModelProperty("提审室背景")
    private String roomBackColor;
    @ApiModelProperty("场次")
    private Integer numOrder;
    @ApiModelProperty("时间段，1表示上午，2表示下午")
    private String timePie;
    @ApiModelProperty("被提审人")
    private String criminalName;
    @ApiModelProperty("犯罪类型")
    private String criminalType;
    @ApiModelProperty("案件阶段")
    private String stage;
    private String remark;
    private String startTime;

}
