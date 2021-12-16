package com.mkst.umap.app.admin.api.bean.param.vacation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName VacationParam
 * @Description 请假相关参数
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-24 17:14
 */
@Data
@ApiModel(value = "请假相关参数", description = "请假的公共参数")
public class VacationParam {

    @ApiModelProperty(value = "申请id", example = "1")
    private Long id;

    @ApiModelProperty(value = "批量id", example = "[1,3,5]")
    private Long[] idArr;

    @ApiModelProperty(value = "请假类型", example = "1")
    private String type;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间", example = "2020-08-24 19:00:00")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间", example = "2020-08-27 19:00:00")
    private Date endTime;

    @ApiModelProperty(value = "事由",example = "回家结婚")
    private String content;

    @ApiModelProperty(value = "是否取消",example = "0")
    private String status;

    @ApiModelProperty(value = "审核状态",example = "0")
    private String auditStatus;

    @ApiModelProperty(value = "审核人id",example = "1")
    private Long approveTo;

    @ApiModelProperty(value = "检查时间",example = "2020-08-24 00:00:00")
    private Date checkDate;

    @ApiModelProperty(value = "审核",example = "不好")
    private String reason;
}
