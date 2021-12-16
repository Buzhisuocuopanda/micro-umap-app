package com.mkst.umap.app.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mkst.umap.app.admin.mapper.PetitionPersonnelBindMapper;
import com.mkst.umap.app.admin.domain.PetitionPersonnelBind;
import com.mkst.umap.app.admin.service.IPetitionPersonnelBindService;
import com.mkst.mini.systemplus.common.support.Convert;

/**
 * 信访与人员连接 服务层实现
 * 
 * @author wangyong
 * @date 2020-08-25
 */
@Service
public class PetitionPersonnelBindServiceImpl implements IPetitionPersonnelBindService 
{
	@Autowired
	private PetitionPersonnelBindMapper petitionPersonnelBindMapper;

	/**
     * 查询信访与人员连接信息
     * 
     * @param id 信访与人员连接ID
     * @return 信访与人员连接信息
     */
    @Override
	public PetitionPersonnelBind selectPetitionPersonnelBindById(Long id)
	{
	    return petitionPersonnelBindMapper.selectPetitionPersonnelBindById(id);
	}
	
	/**
     * 查询信访与人员连接列表
     * 
     * @param petitionPersonnelBind 信访与人员连接信息
     * @return 信访与人员连接集合
     */
	@Override
	public List<PetitionPersonnelBind> selectPetitionPersonnelBindList(PetitionPersonnelBind petitionPersonnelBind)
	{
	    return petitionPersonnelBindMapper.selectPetitionPersonnelBindList(petitionPersonnelBind);
	}
	
    /**
     * 新增信访与人员连接
     * 
     * @param petitionPersonnelBind 信访与人员连接信息
     * @return 结果
     */
	@Override
	public int insertPetitionPersonnelBind(PetitionPersonnelBind petitionPersonnelBind)
	{
	    return petitionPersonnelBindMapper.insertPetitionPersonnelBind(petitionPersonnelBind);
	}
	
	/**
     * 修改信访与人员连接
     * 
     * @param petitionPersonnelBind 信访与人员连接信息
     * @return 结果
     */
	@Override
	public int updatePetitionPersonnelBind(PetitionPersonnelBind petitionPersonnelBind)
	{
	    return petitionPersonnelBindMapper.updatePetitionPersonnelBind(petitionPersonnelBind);
	}

	/**
     * 删除信访与人员连接对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deletePetitionPersonnelBindByIds(String ids)
	{
		return petitionPersonnelBindMapper.deletePetitionPersonnelBindByIds(Convert.toStrArray(ids));
	}

	@Override
	public int insert(Long petitionId, String personnelIds, Long userId) {

		PetitionPersonnelBind insertBind = new PetitionPersonnelBind();
		insertBind.setPetitionId(petitionId);

		String[] personnelIdArr = personnelIds.split(",");
		for (String personnelId : personnelIdArr){
			insertBind.setPersonnelId(Long.valueOf(personnelId));
			this.insertPetitionPersonnelBind(insertBind);
		}

		return personnelIdArr.length;
	}
}
