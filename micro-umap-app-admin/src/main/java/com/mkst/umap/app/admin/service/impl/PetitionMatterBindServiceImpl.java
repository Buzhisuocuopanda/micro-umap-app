package com.mkst.umap.app.admin.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mkst.umap.app.admin.mapper.PetitionMatterBindMapper;
import com.mkst.umap.app.admin.domain.PetitionMatterBind;
import com.mkst.umap.app.admin.service.IPetitionMatterBindService;
import com.mkst.mini.systemplus.common.support.Convert;

/**
 * 信访民行申诉与事项连接 服务层实现
 * 
 * @author wangyong
 * @date 2020-08-25
 */
@Service
public class PetitionMatterBindServiceImpl implements IPetitionMatterBindService 
{
	@Autowired
	private PetitionMatterBindMapper petitionMatterBindMapper;

	/**
     * 查询信访民行申诉与事项连接信息
     * 
     * @param id 信访民行申诉与事项连接ID
     * @return 信访民行申诉与事项连接信息
     */
    @Override
	public PetitionMatterBind selectPetitionMatterBindById(Long id)
	{
	    return petitionMatterBindMapper.selectPetitionMatterBindById(id);
	}
	
	/**
     * 查询信访民行申诉与事项连接列表
     * 
     * @param petitionMatterBind 信访民行申诉与事项连接信息
     * @return 信访民行申诉与事项连接集合
     */
	@Override
	public List<PetitionMatterBind> selectPetitionMatterBindList(PetitionMatterBind petitionMatterBind)
	{
	    return petitionMatterBindMapper.selectPetitionMatterBindList(petitionMatterBind);
	}
	
    /**
     * 新增信访民行申诉与事项连接
     * 
     * @param petitionMatterBind 信访民行申诉与事项连接信息
     * @return 结果
     */
	@Override
	public int insertPetitionMatterBind(PetitionMatterBind petitionMatterBind)
	{
	    return petitionMatterBindMapper.insertPetitionMatterBind(petitionMatterBind);
	}
	
	/**
     * 修改信访民行申诉与事项连接
     * 
     * @param petitionMatterBind 信访民行申诉与事项连接信息
     * @return 结果
     */
	@Override
	public int updatePetitionMatterBind(PetitionMatterBind petitionMatterBind)
	{
	    return petitionMatterBindMapper.updatePetitionMatterBind(petitionMatterBind);
	}

	/**
     * 删除信访民行申诉与事项连接对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deletePetitionMatterBindByIds(String ids)
	{
		return petitionMatterBindMapper.deletePetitionMatterBindByIds(Convert.toStrArray(ids));
	}

	@Override
	public int insert(Long petitionId, String matterIds, Long userId) {

		PetitionMatterBind insertMatterBind = new PetitionMatterBind();
		insertMatterBind.setPetitionId(petitionId);

		String[] matterIdArr = matterIds.split(",");
		for(String matterId : matterIdArr){
			insertMatterBind.setMatterId(Long.valueOf(matterId));
			this.insertPetitionMatterBind(insertMatterBind);
		}

		return matterIdArr.length;
	}
}
