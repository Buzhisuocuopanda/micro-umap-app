package com.mkst.umap.app.admin.domain;

import com.mkst.mini.systemplus.common.base.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 提审操作日志表 umap_arraign_operate_record
 * 
 * @author
 * @date 2021-03-22
 */
@Data
public class ArraignOperateRecord extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 操作记录 */
	private String operateFunction;
	/*罪犯姓名*/
	private String criminalName;
	/*犯罪类型*/
	private String crimeType;
	/*原案预约时间*/
	private String appointmentTime;
	/**  */
	private Date createTime;
	/**  */
	private String createBy;
	/**  */
	private Date updateTime;
	/**  */
	private String updateBy;
	/**  */
	private String remark;

}
