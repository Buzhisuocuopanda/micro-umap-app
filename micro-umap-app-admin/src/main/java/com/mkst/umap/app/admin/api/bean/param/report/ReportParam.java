package com.mkst.umap.app.admin.api.bean.param.report;

import com.mkst.umap.app.common.enums.BusinessTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName ReportParam
 * @Description 举报相关参数
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-27 14:26
 */
@Data
@ApiModel(value = "举报相关参数",description = "举报相关的统一参数")
public class ReportParam {

    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    @ApiModelProperty(value = "举报类型",example = "umap_report_food_medicine")
    private String type;

    @ApiModelProperty(value = "是否已回复" ,example = "0",hidden = true,notes = "留存字段")
    private String hasReplied;

    @ApiModelProperty(value = "举报地址", example = "东门")
    private String address;

    @ApiModelProperty(value = "是否匿名", example = "1")
    private String realName;

    @ApiModelProperty(value = "详细内容", example = "测试举报")
    private String content;

    @ApiModelProperty(value = "附件ids", example = "1,2,3")
    private String fileEntityIds;

    @ApiModelProperty(value = "创建人id", example = "1")
    private Long createBy;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "检查时间", example = "2020-08-27 00:00:00", hidden = true)
    private Date checkDate;
}
