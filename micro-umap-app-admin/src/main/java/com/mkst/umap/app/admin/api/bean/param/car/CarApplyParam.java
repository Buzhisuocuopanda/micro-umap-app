package com.mkst.umap.app.admin.api.bean.param.car;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mkst.mini.systemplus.common.utils.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@ApiModel(value = "车辆审批传参对象")
@Data
public class CarApplyParam {

    @ApiModelProperty(value = "车辆预约申请id", hidden = true)
    private Long carApplyId;

    @ApiModelProperty(value = "申请人id(用户id)")
    private Long userId;

    @ApiModelProperty(value = "联系方式")
    private String telphone;

    @ApiModelProperty(value = "是否需要司机")
    private Boolean driverWhether;

    @ApiModelProperty(value = "人数（含司机）")
    private Integer peopleNumber;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "地区选择")
    private String areaSelect;

    @ApiModelProperty(value = "理由")
    private String remark;

    @ApiModelProperty(value = "起点")
    private MapLocationParam startPoint;

    @ApiModelProperty(value = "终点")
    private MapLocationParam endPoint;

    @ApiModelProperty(value = "审核人")
    private Integer approvalUserId;

    public void setNeedExtraApproval(Boolean needExtraApproval) {
        this.needExtraApproval = needExtraApproval;
    }

    public void setNeedExtraApproval() {
        this.needExtraApproval = this.areaSelect.equals("市外") && this.isTimeOver1Day();
    }

    /**
     * 是否需要额外审批
     */
    private Boolean needExtraApproval;

    public Boolean getNeedExtraApproval() {
        Boolean isTimeOver1Day = isTimeOver1Day();
        if (this.needExtraApproval == null && isTimeOver1Day != null) {
            return "市外".equals(this.areaSelect) && isTimeOver1Day;
        } else {
            return needExtraApproval;
        }
    }

    public Boolean isTimeOver1Day() {
        if (this.startTime != null && this.endTime != null) {
            return !DateUtils.isSameDay(startTime, endTime);
        } else {
            return null;
        }
    }
}
