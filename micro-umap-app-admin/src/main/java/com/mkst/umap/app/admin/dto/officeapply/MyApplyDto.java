package com.mkst.umap.app.admin.dto.officeapply;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName MyApplyDto
 * @Description 办公申请-我的申请
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-10 11:07
 */
@Data
public class MyApplyDto {
    /**
     * 申请id
     */
    private Long id;
    /**
     * 办公申请类型
     */
    private String type;
    /**
     * 标题
     */
    private String title;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 申请状态
     */
    private String status;

    /**
     * 创建人姓名
     */
    private String auditStatus;

}
