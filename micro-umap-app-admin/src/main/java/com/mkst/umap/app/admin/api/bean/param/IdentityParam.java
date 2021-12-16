package com.mkst.umap.app.admin.api.bean.param;

import lombok.Data;

@Data
public class IdentityParam {

    /** 身份类型 */
    private Integer userType;
    /** 姓名 */
    private String name;
    /** 资格证号  */
    private String certNo;
    /** 单位名称 */
    private String companyName;

    private String fileIds;

    private Integer userId;
}
