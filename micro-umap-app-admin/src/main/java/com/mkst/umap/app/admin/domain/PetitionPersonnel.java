package com.mkst.umap.app.admin.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.mkst.mini.systemplus.common.base.BaseEntity;
import java.util.Date;

/**
 * 信访相关人员表 umap_petition_personnel
 * 
 * @author wangyong
 * @date 2020-08-25
 */
@Data
public class PetitionPersonnel
{
	private static final long serialVersionUID = 1L;
	
	/** id */
	private Long id;
	/** 人员类型 */
	private String type;
	/** 人员姓名 */
	private String name;
	/** 性别 */
	private String sex;
	/** 与原案件关系 */
	private String relationToCase;
	/** 证件类型（默认身份证） */
	private String credentialsType;
	/** 证件号码 */
	private String credentialsNum;
	/** 国籍 */
	private String country;
	/** 民族 */
	private String nation;
	/** 联系电话 */
	private String phoneNum;
	/** 电子邮箱 */
	private String email;
	/** 工作单位 */
	private String employer;
	/** 工作单位地址 */
	private String employerAddress;
	/** 居住地址 */
	private String residentialAddress;
	/** 身份 */
	private String identity;
	/** 职位 */
	private String post;
	/** 法定代表姓名 */
	private String legalRepresentativeName;
	/** 法定代表职务 */
	private String legalRepresentativePost;
	/** 送达地址 */
	private String deliveryAddress;
	/** 创建人 */
	private Long createBy;
	/** 创建时间 */
	private Date createTime;
	/** 更新者 */
	private Long updateBy;
	/** 更新时间 */
	private Date updateTime;
	/** 备注 */
	private String remark;
	/** 删除标识 */
	private String delFlag;
}
