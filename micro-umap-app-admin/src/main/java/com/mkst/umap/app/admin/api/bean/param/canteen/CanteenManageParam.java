package com.mkst.umap.app.admin.api.bean.param.canteen;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@ApiModel(value = "食堂预约申请传参对象")
public class CanteenManageParam {

    @ApiModelProperty(value = "食堂申请id", hidden = true)
    private Long canteenApplyId;

    @ApiModelProperty(value = "包厢餐次id")
    private Long boxMealId;

    @ApiModelProperty(value = "包厢id")
    private String boxId;

    @ApiModelProperty(value = "餐次")
    private String meal;

    @ApiModelProperty(value = "用餐类型")
    private String diningType;

    @ApiModelProperty(value = "用餐人数")
    private Integer diningNumber;

    @ApiModelProperty(value = "用餐状态")
    private String diningStatus;

    @ApiModelProperty(value = "申请状态Arr")
    private String[] applyStatusArr;

    @ApiModelProperty(value = "申请人id(用户id)")
    private Long userId;

    @ApiModelProperty(value = "使用部门id", required = true)
    private Long deptId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "日期", required = true)
    private String dateTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "日期", required = true)
    private String date;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "年份",example = "2020",name = "年份")
    private String year;

    @ApiModelProperty(value = "月份",example = "12",name = "月份")
    private String month;

    @ApiModelProperty(value = "状态",example = "1",name = "状态")
    private String status;

    @ApiModelProperty(value = "当前用户id", hidden = true)
    private Long nowUserId;

    @ApiModelProperty(value = "目标审核人员id", example = "3210")
    private Long targetId;

    @ApiModelProperty(value = "事件code",example = "6")
    private String eventCode;

    @ApiModelProperty(value = "审批人登录名", example = "113113")
    private String approvalToLoginName;
}
