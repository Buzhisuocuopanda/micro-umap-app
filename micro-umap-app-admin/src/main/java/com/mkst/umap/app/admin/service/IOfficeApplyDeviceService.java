package com.mkst.umap.app.admin.service;

import com.mkst.umap.app.admin.domain.OfficeApplyDevice;

import java.util.List;

/**
 * 办公申请设备 服务层
 * 
 * @author wangyong
 * @date 2020-11-17
 */
public interface IOfficeApplyDeviceService 
{
	/**
     * 查询办公申请设备信息
     * 
     * @param id 办公申请设备ID
     * @return 办公申请设备信息
     */
	public OfficeApplyDevice selectOfficeApplyDeviceById(Long id);
	
	/**
     * 查询办公申请设备列表
     * 
     * @param officeApplyDevice 办公申请设备信息
     * @return 办公申请设备集合
     */
	public List<OfficeApplyDevice> selectOfficeApplyDeviceList(OfficeApplyDevice officeApplyDevice);
	
	/**
     * 新增办公申请设备
     * 
     * @param officeApplyDevice 办公申请设备信息
     * @return 结果
     */
	public int insertOfficeApplyDevice(OfficeApplyDevice officeApplyDevice);
	
	/**
     * 修改办公申请设备
     * 
     * @param officeApplyDevice 办公申请设备信息
     * @return 结果
     */
	public int updateOfficeApplyDevice(OfficeApplyDevice officeApplyDevice);
		
	/**
     * 删除办公申请设备信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteOfficeApplyDeviceByIds(String ids);
	
}
