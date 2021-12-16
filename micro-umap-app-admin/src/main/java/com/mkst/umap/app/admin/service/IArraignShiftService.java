package com.mkst.umap.app.admin.service;

import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.umap.app.admin.domain.ArraignShift;
import java.util.List;

/**
 * 提审换班 服务层
 * 
 * @author wangyong
 * @date 2021-04-15
 */
public interface IArraignShiftService 
{
	/**
     * 查询提审换班信息
     * 
     * @param id 提审换班ID
     * @return 提审换班信息
     */
	public ArraignShift selectArraignShiftById(Integer id);
	
	/**
     * 查询提审换班列表
     * 
     * @param arraignShift 提审换班信息
     * @return 提审换班集合
     */
	public List<ArraignShift> selectArraignShiftList(ArraignShift arraignShift);

	public Integer selectArraignShiftCount(ArraignShift arraignShift);

	public Integer selectArraignShiftCountByTargetUserId(SysUser sysUser, String status);
	
	/**
     * 新增提审换班
     * 
     * @param arraignShift 提审换班信息
     * @return 结果
     */
	public int insertArraignShift(ArraignShift arraignShift);
	
	/**
     * 修改提审换班
     * 
     * @param arraignShift 提审换班信息
     * @return 结果
     */
	public int updateArraignShift(ArraignShift arraignShift);

	public int auditArraignShift(Integer id,String status);
		
	/**
     * 删除提审换班信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteArraignShiftByIds(String ids);

	void changeShift(Integer id);
}
