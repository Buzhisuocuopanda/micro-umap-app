package com.mkst.umap.app.admin.api.bean.result.car;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Auther: ltt
 * @Date: 2020/09/03/20:21
 * @Description:
 */
@Data
public class AuditParam {

    private String userName;

    private String sex;

    private Integer approveStatus;

    private String approveType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;

    private String url;
}
