package com.mkst.umap.app.admin.api.bean.param;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName SelectProcustorParam
 * @Description
 * @Author yinyuanping
 * @Modified By:
 * @Date 2021/5/26 0026 下午 5:37
 */
@Data
public class SelectProcustorParam {

    private Date checkDate;
    private Long prosecutorUserId;
    private String createBy;
    private Date limitDate;

}
