package com.mkst.umap.app.admin.service.impl;

import com.mkst.mini.systemplus.common.support.Convert;
import com.mkst.umap.app.admin.domain.ArraignOperateRecord;
import com.mkst.umap.app.admin.mapper.ArraignOperateRecordMapper;
import com.mkst.umap.app.admin.service.IArraignOperateRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 提审操作日志 服务层实现
 * 
 * @author wangyong
 * @date 2021-04-06
 */
@Service
public class ArraignOperateRecordServiceImpl implements IArraignOperateRecordService 
{
	@Autowired
	private ArraignOperateRecordMapper arraignOperateRecordMapper;

	/**
     * 查询提审操作日志信息
     * 
     * @param id 提审操作日志ID
     * @return 提审操作日志信息
     */
    @Override
	public ArraignOperateRecord selectArraignOperateRecordById(Integer id)
	{
	    return arraignOperateRecordMapper.selectArraignOperateRecordById(id);
	}
	
	/**
     * 查询提审操作日志列表
     * 
     * @param arraignOperateRecord 提审操作日志信息
     * @return 提审操作日志集合
     */
	@Override
	public List<ArraignOperateRecord> selectArraignOperateRecordList(ArraignOperateRecord arraignOperateRecord)
	{
	    return arraignOperateRecordMapper.selectArraignOperateRecordList(arraignOperateRecord);
	}
	
    /**
     * 新增提审操作日志
     * 
     * @param arraignOperateRecord 提审操作日志信息
     * @return 结果
     */
	@Override
	public int insertArraignOperateRecord(ArraignOperateRecord arraignOperateRecord)
	{
	    return arraignOperateRecordMapper.insertArraignOperateRecord(arraignOperateRecord);
	}
	
	/**
     * 修改提审操作日志
     * 
     * @param arraignOperateRecord 提审操作日志信息
     * @return 结果
     */
	@Override
	public int updateArraignOperateRecord(ArraignOperateRecord arraignOperateRecord)
	{
	    return arraignOperateRecordMapper.updateArraignOperateRecord(arraignOperateRecord);
	}

	/**
     * 删除提审操作日志对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteArraignOperateRecordByIds(String ids)
	{
		return arraignOperateRecordMapper.deleteArraignOperateRecordByIds(Convert.toStrArray(ids));
	}
	
}
