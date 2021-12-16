package com.mkst.umap.app.admin.api.bean.result;

import lombok.Data;

/**
 * @ClassName NameCountResult
 * @Description 一些用于统计数量的公用result
 * @Author wangyong
 * @Modified By:
 * @Date 2020-10-22 10:19
 */
@Data
public class NameCountResult {
    private String name;
    private Long count;
    private String info;
    private boolean status;
}
