package com.mkst.umap.app.admin.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mkst.umap.app.admin.mapper.QrCodeManageMapper;
import com.mkst.umap.app.admin.domain.QrCodeManage;
import com.mkst.umap.app.admin.service.IQrCodeManageService;
import com.mkst.mini.systemplus.common.support.Convert;

/**
 * 二维码管理 服务层实现
 * 
 * @author wangyong
 * @date 2020-07-13
 */
@Service
public class QrCodeManageServiceImpl implements IQrCodeManageService 
{
	@Autowired
	private QrCodeManageMapper qrCodeManageMapper;

	/**
     * 查询二维码管理信息
     * 
     * @param qrCodeId 二维码管理ID
     * @return 二维码管理信息
     */
    @Override
	public QrCodeManage selectQrCodeManageById(Long qrCodeId)
	{
	    return qrCodeManageMapper.selectQrCodeManageById(qrCodeId);
	}
	
	/**
     * 查询二维码管理列表
     * 
     * @param qrCodeManage 二维码管理信息
     * @return 二维码管理集合
     */
	@Override
	public List<QrCodeManage> selectQrCodeManageList(QrCodeManage qrCodeManage)
	{
	    return qrCodeManageMapper.selectQrCodeManageList(qrCodeManage);
	}
	
    /**
     * 新增二维码管理
     * 
     * @param qrCodeManage 二维码管理信息
     * @return 结果
     */
	@Override
	public int insertQrCodeManage(QrCodeManage qrCodeManage)
	{
	    return qrCodeManageMapper.insertQrCodeManage(qrCodeManage);
	}
	
	/**
     * 修改二维码管理
     * 
     * @param qrCodeManage 二维码管理信息
     * @return 结果
     */
	@Override
	public int updateQrCodeManage(QrCodeManage qrCodeManage)
	{
	    return qrCodeManageMapper.updateQrCodeManage(qrCodeManage);
	}

	/**
     * 删除二维码管理对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteQrCodeManageByIds(String ids)
	{
		return qrCodeManageMapper.deleteQrCodeManageByIds(Convert.toStrArray(ids));
	}
	
}
