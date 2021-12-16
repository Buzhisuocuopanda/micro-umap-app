package com.mkst.umap.app.admin.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mkst.mini.systemplus.common.base.BaseEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * 食堂申请表 umap_canteen_manage
 * 
 * @author wangyong
 * @date 2020-07-20
 */
@Data
public class CanteenManage extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 食堂申请id */
	private Long canteenApplyId;
	/** 包厢餐次id */
	private Long boxMealId;
	/** 用餐类型 */
	private String diningType;
	/** 用餐人数 */
	private Integer diningNumber;
	/** 用餐状态 */
	private Integer diningStatus;
	/** 申请状态 0 待审核  1 通过  2失败  3取消*/
	private Integer applyStatus;
	/** 日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String dateTime;
	/** 申请人id(用户id) */
	private Long userId;
	/** 使用部门id */
	private Long deptId;
	/** 逻辑删除标识（0：正常 1：删除）*/
	private String delFlag;
	//仅用于数据传递
	/** 包厢id */
	private String boxId;
	/** 包厢名 */
	private String boxName;
	/*进程*/
	private Integer progress;
	/** 状态查询集合 */
	private List<Integer> applyStatusList;
	/** 部门名 */
	private String deptName;
	/** 申请人 */
	private String applyName;
	/** 创建人姓名 */
	private String createName;
	/** 餐次 */
	private String meal;
	/*备注*/
	private String remark;
}
