package com.mkst.umap.app.admin.api.bean.param.petition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName PersonnelParam
 * @Description 信访相关人员
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-25 20:33
 */
@Data
@ApiModel(value = "信访相关人员参数",description = "信访相关人员的参数")
public class PersonnelParam {
    @ApiModelProperty(value = "人员id",example = "1")
    private Long id;

    @ApiModelProperty(value = "人员类型",example = "accuse_accuser",required = true)
    private String type;

    @ApiModelProperty(value = "人员姓名",example = "张三")
    private String name;

    @ApiModelProperty(value = "性别",example = "1")
    private String sex;

    @ApiModelProperty(value = "与原案件关系",example = "目击证人")
    private String relationToCase;

    @ApiModelProperty(value = "证件类型",example = "10")
    private String credentialsType;

    @ApiModelProperty(value = "证件号码",example = "41282200000000")
    private String credentialsNum;

    @ApiModelProperty(value = "国籍",example = "中国")
    private String country;

    @ApiModelProperty(value = "民族",example = "汉")
    private String nation;

    @ApiModelProperty(value = "联系电话",example = "13888888888")
    private String phoneNum;

    @ApiModelProperty(value = "电子邮箱",example = "123@qq.com")
    private String email;

    @ApiModelProperty(value = "工作单位",example = "检察院")
    private String employer;

    @ApiModelProperty(value = "工作单位地址",example = "兴东")
    private String employerAddress;

    @ApiModelProperty(value = "居住地址",example = "兴东")
    private String residentialAddress;

    @ApiModelProperty(value = "身份",example = "检察官")
    private String identity;

    @ApiModelProperty(value = "职位",example = "院长")
    private String post;

    @ApiModelProperty(value = "法定代表姓名",example = "李四")
    private String legalRepresentativeName;

    @ApiModelProperty(value = "法定代表职务",example = "局长")
    private String legalRepresentativePost;

    @ApiModelProperty(value = "送达地址",example = "兴东")
    private String deliveryAddress;
}
