package com.mkst.umap.app.admin.api.bean.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName MyAppointmentParam
 * @Description
 * @Author wangyong
 * @Date 2020-06-23 14:29
 */
@Data
public class MyAppointmentParam {
    private String loginName;
    private String status;
    private String nowStatus;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkDate;
    private String auditStatus;
}
