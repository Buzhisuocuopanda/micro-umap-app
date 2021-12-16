package com.mkst.umap.app.admin.dto.arraign;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName UnauditInfoDto
 * @Description 返回审批列表
 * @Author wangyong
 * @Date 2020-06-29 09:46
 */
@Data
public class AuditInfoDto {
    /**
     * 预约id
     */
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date endTime;

    /**
     * 申请人
     */
    private String createBy;
    /** 申请部门 */
    private String dept;
    /**
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    /**
     * 审核状态
     */
    private String status;

    //被提神人
    private String criminalName;
    //犯罪类型
    private String criminalType;
    //案件阶段
    private String stage;

    private String prosecutorName;

    private String useByName;

    private String remark;

    public String getUseByName(){
        return prosecutorName;
    }
}
