package com.mkst.umap.app.admin.api.bean.param.canteen;

import com.mkst.umap.app.admin.domain.BoxMeal;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: ltt
 * @Date: 2020/09/02/15:22
 * @Description:
 */
@Data
public class BoxMealParam {

    @ApiModelProperty(value = "包厢餐次实体")
    private BoxMeal boxMeal;

    @ApiModelProperty(value = "这个餐次是否可预约")
    private Boolean status;
}
