package com.mkst.umap.app.admin.api.bean.param.qrCodeManage;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "二维码管理传参对象")
public class QrCodeManageParam {

    @ApiModelProperty(value = "二维码id", hidden = true)
    private Integer qrCodeId;

    @ApiModelProperty(value = "业务id")
    private Integer businessId;

    @ApiModelProperty(value = "业务事项")
    private String businessMatter;

    @ApiModelProperty(value = "跳转url")
    private String jumpUrl;

    @ApiModelProperty(value = "是否需要跳转")
    private Boolean jumpWhether;

    @ApiModelProperty(value = "是否加密")
    private Boolean encryptWhether;

    @ApiModelProperty(value = "二维码地址")
    private String qrCodeAddress;

    @ApiModelProperty(value = "加密数据区", required = true)
    private String encryptDataArea;

    @ApiModelProperty(value = "备注")
    private String remark;

}
