package com.mkst.umap.app.admin.service;

import com.mkst.umap.app.admin.domain.QrCodeManage;
import java.util.List;

/**
 * 二维码管理 服务层
 * 
 * @author wangyong
 * @date 2020-07-13
 */
public interface IQrCodeManageService 
{
	/**
     * 查询二维码管理信息
     * 
     * @param qrCodeId 二维码管理ID
     * @return 二维码管理信息
     */
	public QrCodeManage selectQrCodeManageById(Long qrCodeId);
	
	/**
     * 查询二维码管理列表
     * 
     * @param qrCodeManage 二维码管理信息
     * @return 二维码管理集合
     */
	public List<QrCodeManage> selectQrCodeManageList(QrCodeManage qrCodeManage);
	
	/**
     * 新增二维码管理
     * 
     * @param qrCodeManage 二维码管理信息
     * @return 结果
     */
	public int insertQrCodeManage(QrCodeManage qrCodeManage);
	
	/**
     * 修改二维码管理
     * 
     * @param qrCodeManage 二维码管理信息
     * @return 结果
     */
	public int updateQrCodeManage(QrCodeManage qrCodeManage);
		
	/**
     * 删除二维码管理信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteQrCodeManageByIds(String ids);
	
}
