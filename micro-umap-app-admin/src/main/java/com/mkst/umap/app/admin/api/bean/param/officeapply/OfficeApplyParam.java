package com.mkst.umap.app.admin.api.bean.param.officeapply;

import com.mkst.umap.app.admin.domain.OfficeApplyDevice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @ClassName OfficeApplyParam
 * @Description 办公申请相关的参数
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-07 14:41
 */
@Data
@ApiModel(value = "办公申请相关参数", description = "办公申请的共用参数")
public class OfficeApplyParam {

    @ApiModelProperty(value = "申请id", example = "1")
    private Long id;

    @ApiModelProperty(value = "申请idArr(此参数与id只可保留一个，否则会失效)", example = "[1,3,5]")
    private Long[] idArr;

    @ApiModelProperty(value = "办公申请类型（字典：office_apply_type）", example = "1")
    private String type;

    @ApiModelProperty(value = "申请部门id", example = "1")
    private Long deptId;

    @ApiModelProperty(value = "申请标题", example = "东门地区出警申请")
    private String title;

    @ApiModelProperty(value = "详细内容", example = "东门地区发生重大恶意伤人事件")
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间", example = "2020-05-07 14:30:00")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间", example = "2020-05-07 19:30:00")
    private Date endTime;

    @ApiModelProperty(value = "目的地", example = "东门")
    private String destination;

    @ApiModelProperty(value = "审批人ID", example = "1")
    private Long approvalTo;

    @ApiModelProperty(value = "审批人登录名", example = "113113")
    private String approvalToLoginName;

    @ApiModelProperty(value = "当前状态（字典：event_cancel）", example = "0")
    private String status;

    @ApiModelProperty(value = "审核状态（字典：event_audit_status）", example = "1")
    private String auditStatus;

    @ApiModelProperty(value = "创建者登录名", example = "admin")
    private String createBy;

    @ApiModelProperty(value = "更新者登录名", example = "admin")
    private String updateBy;

    @ApiModelProperty(value = "删除标志（暂不使用）", example = "0", hidden = true)
    private String delFlag;

    @ApiModelProperty(value = "备注（暂不使用）", example = "备注", hidden = true)
    private String remark;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "查询创建时间", example = "2020-08-15 00:00:00")
    private Date checkCreateDate;

    @ApiModelProperty(value = "目标审核人员id", example = "3210")
    private Long targetId;

    @ApiModelProperty(value = "事件code",example = "6")
    private String eventCode;

    @ApiModelProperty(value = "审核理由",example = "不好")
    private String reason;

    private List<OfficeApplyDevice> deviceList;
}
