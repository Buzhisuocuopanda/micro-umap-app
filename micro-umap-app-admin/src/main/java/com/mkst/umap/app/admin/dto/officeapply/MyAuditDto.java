package com.mkst.umap.app.admin.dto.officeapply;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName MyAuditDto
 * @Description 办公申请-我的审批
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-07 17:05
 */
@Data
public class MyAuditDto {

    /**
     * 申请id
     */
    private Long id;

    /**
     * 办公申请类型
     */
    private String type;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date endTime;

    /**
     * 创建人姓名
     */
    private String createByName;

    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String auditStatus;

}
