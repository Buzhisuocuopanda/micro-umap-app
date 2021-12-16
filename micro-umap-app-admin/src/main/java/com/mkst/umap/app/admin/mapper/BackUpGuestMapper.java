package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.domain.BackUpGuest;
import com.mkst.umap.app.admin.domain.vo.BackUpGuestVo;

import java.util.List;

/**
 * 备勤间使用人 数据层
 * 
 * @author lijinghui
 * @date 2020-06-17
 */
public interface BackUpGuestMapper
{
	/**
     * 查询备勤间使用人信息
     * 
     * @param guestId 备勤间使用人ID
     * @return 备勤间使用人信息
     */
	BackUpGuest selectBackUpGuestById(Integer guestId);
	
	/**
     * 查询备勤间使用人列表
     * 
     * @param backUpGuest 备勤间使用人信息
     * @return 备勤间使用人集合
     */
	List<BackUpGuest> selectBackUpGuestList(BackUpGuest backUpGuest);

	/**
	 * 通过申请id获取使用人集合
	 * @param applyId 申请id
	 * @return 备勤间使用人集合
	 */
	List<BackUpGuest> selectBackUpGuestListByApplyId(Long applyId);

	/**
	 * 通过申请id获取使用人视图集合
	 * @param applyId
	 * @return
	 */
	List<BackUpGuestVo> selectGuestVoList(Long applyId);
	
	/**
     * 新增备勤间使用人
     * 
     * @param backUpGuest 备勤间使用人信息
     * @return 结果
     */
	int insertBackUpGuest(BackUpGuest backUpGuest);
	
	/**
     * 修改备勤间使用人
     * 
     * @param backUpGuest 备勤间使用人信息
     * @return 结果
     */
	int updateBackUpGuest(BackUpGuest backUpGuest);
	
	/**
     * 删除备勤间使用人
     * 
     * @param guestId 备勤间使用人ID
     * @return 结果
     */
	int deleteBackUpGuestById(Integer guestId);
	
	/**
     * 批量删除备勤间使用人
     * 
     * @param guestIds 需要删除的数据ID
     * @return 结果
     */
	int deleteBackUpGuestByIds(String[] guestIds);

	/**
	 * 批量新增备勤间使用人信息
	 *
	 * @param list 备勤间使用人列表
	 * @return 结果
	 */
	int batchBackUpGuest(List<BackUpGuest> list);
	
}