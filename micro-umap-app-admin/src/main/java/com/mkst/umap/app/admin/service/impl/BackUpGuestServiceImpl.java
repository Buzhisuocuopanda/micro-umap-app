package com.mkst.umap.app.admin.service.impl;

import com.mkst.mini.systemplus.common.support.Convert;
import com.mkst.umap.app.admin.domain.BackUpGuest;
import com.mkst.umap.app.admin.domain.vo.BackUpGuestVo;
import com.mkst.umap.app.admin.mapper.BackUpGuestMapper;
import com.mkst.umap.app.admin.service.IBackUpGuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 备勤间使用人 服务层实现
 * 
 * @author lijinghui
 * @date 2020-06-17
 */
@Service
public class BackUpGuestServiceImpl implements IBackUpGuestService
{
	@Autowired
	private BackUpGuestMapper backUpGuestMapper;

	/**
     * 查询备勤间使用人信息
     * 
     * @param guestId 备勤间使用人ID
     * @return 备勤间使用人信息
     */
    @Override
	public BackUpGuest selectBackUpGuestById(Integer guestId)
	{
	    return backUpGuestMapper.selectBackUpGuestById(guestId);
	}
	
	/**
     * 查询备勤间使用人列表
     * 
     * @param backUpGuest 备勤间使用人信息
     * @return 备勤间使用人集合
     */
	@Override
	public List<BackUpGuest> selectBackUpGuestList(BackUpGuest backUpGuest)
	{
	    return backUpGuestMapper.selectBackUpGuestList(backUpGuest);
	}

	/**
	 * 通过申请id获取使用人集合
	 * @param applyId
	 * @return
	 */
	@Override
	public List<BackUpGuest> selectBackUpGuestListByApplyId(Long applyId){
		return backUpGuestMapper.selectBackUpGuestListByApplyId(applyId);
	}

	/**
	 * 通过申请id获取使用人视图集合
	 * @param applyId
	 * @return
	 */
	@Override
	public List<BackUpGuestVo> selectGuestVoList(Long applyId){
		return backUpGuestMapper.selectGuestVoList(applyId);
	}
	
    /**
     * 新增备勤间使用人
     * 
     * @param backUpGuest 备勤间使用人信息
     * @return 结果
     */
	@Override
	public int insertBackUpGuest(BackUpGuest backUpGuest)
	{
	    return backUpGuestMapper.insertBackUpGuest(backUpGuest);
	}
	
	/**
     * 修改备勤间使用人
     * 
     * @param backUpGuest 备勤间使用人信息
     * @return 结果
     */
	@Override
	public int updateBackUpGuest(BackUpGuest backUpGuest)
	{
	    return backUpGuestMapper.updateBackUpGuest(backUpGuest);
	}

	/**
     * 删除备勤间使用人对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteBackUpGuestByIds(String ids)
	{
		return backUpGuestMapper.deleteBackUpGuestByIds(Convert.toStrArray(ids));
	}

	@Override
	public int batchBackUpGuest(List<BackUpGuest> list){
		return backUpGuestMapper.batchBackUpGuest(list);
	}
	
}
