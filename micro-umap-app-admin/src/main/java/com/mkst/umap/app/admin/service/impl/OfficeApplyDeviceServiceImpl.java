package com.mkst.umap.app.admin.service.impl;

import com.mkst.mini.systemplus.common.support.Convert;
import com.mkst.umap.app.admin.domain.OfficeApplyDevice;
import com.mkst.umap.app.admin.mapper.OfficeApplyDeviceMapper;
import com.mkst.umap.app.admin.service.IOfficeApplyDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 办公申请设备 服务层实现
 * 
 * @author wangyong
 * @date 2020-11-17
 */
@Service
public class OfficeApplyDeviceServiceImpl implements IOfficeApplyDeviceService 
{
	@Autowired
	private OfficeApplyDeviceMapper officeApplyDeviceMapper;

	/**
     * 查询办公申请设备信息
     * 
     * @param id 办公申请设备ID
     * @return 办公申请设备信息
     */
    @Override
	public OfficeApplyDevice selectOfficeApplyDeviceById(Long id)
	{
	    return officeApplyDeviceMapper.selectOfficeApplyDeviceById(id);
	}
	
	/**
     * 查询办公申请设备列表
     * 
     * @param officeApplyDevice 办公申请设备信息
     * @return 办公申请设备集合
     */
	@Override
	public List<OfficeApplyDevice> selectOfficeApplyDeviceList(OfficeApplyDevice officeApplyDevice)
	{
	    return officeApplyDeviceMapper.selectOfficeApplyDeviceList(officeApplyDevice);
	}
	
    /**
     * 新增办公申请设备
     * 
     * @param officeApplyDevice 办公申请设备信息
     * @return 结果
     */
	@Override
	public int insertOfficeApplyDevice(OfficeApplyDevice officeApplyDevice)
	{
	    return officeApplyDeviceMapper.insertOfficeApplyDevice(officeApplyDevice);
	}
	
	/**
     * 修改办公申请设备
     * 
     * @param officeApplyDevice 办公申请设备信息
     * @return 结果
     */
	@Override
	public int updateOfficeApplyDevice(OfficeApplyDevice officeApplyDevice)
	{
	    return officeApplyDeviceMapper.updateOfficeApplyDevice(officeApplyDevice);
	}

	/**
     * 删除办公申请设备对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteOfficeApplyDeviceByIds(String ids)
	{
		return officeApplyDeviceMapper.deleteOfficeApplyDeviceByIds(Convert.toStrArray(ids));
	}
	
}
