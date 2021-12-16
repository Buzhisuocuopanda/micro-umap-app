package com.mkst.umap.app.admin.api.bean.param;

import lombok.Data;

/**
 * @ClassName OrderCountParam
 * @Description TODO
 * @Author wangyong
 * @Modified By:
 * @Date 2020-12-04 16:27
 */
@Data
public class OrderCountParam {
    //0为7天 1为30天
    private Integer type;
}
