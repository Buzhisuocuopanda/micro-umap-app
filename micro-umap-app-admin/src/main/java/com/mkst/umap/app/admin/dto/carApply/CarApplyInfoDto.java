package com.mkst.umap.app.admin.dto.carApply;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mkst.umap.app.admin.dto.apply.BackUpGuestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@ApiModel(value = "车辆申请 数据传递对象")
@Data
public class CarApplyInfoDto {

    /** 车辆预约申请id */
    private Long carApplyId;
    /** 联系方式 */
    private String telphone;
    /** 是否需要司机 */
    private Boolean driverWhether;
    /** 人数（含司机） */
    private Integer peopleNumber;
    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm")
    private Date startTime;
    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm")
    private Date endTime;
    /** 地区选择 */
    private String areaSelect;
    /** 审批状态 */
    private Integer approveStatus;
    /** 接单状态 */
    private Integer driverStatus;
    /**理由*/
    private String remark;

    /** 车辆号 */
    private String carName;
    /** 申请人 */
    private String applicant;
    /** 司机id */
    private String driverName;

    /** 起点位置 */
    private String startLocation;
    /** 终点位置 */
    private String endLocation;

    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm")
    private Date createTime;

    private String createByName;
}
