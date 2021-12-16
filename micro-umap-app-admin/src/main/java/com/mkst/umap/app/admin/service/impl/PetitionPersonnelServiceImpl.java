package com.mkst.umap.app.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mkst.umap.app.admin.mapper.PetitionPersonnelMapper;
import com.mkst.umap.app.admin.domain.PetitionPersonnel;
import com.mkst.umap.app.admin.service.IPetitionPersonnelService;
import com.mkst.mini.systemplus.common.support.Convert;

/**
 * 信访相关人员 服务层实现
 * 
 * @author wangyong
 * @date 2020-08-25
 */
@Service
public class PetitionPersonnelServiceImpl implements IPetitionPersonnelService 
{
	@Autowired
	private PetitionPersonnelMapper petitionPersonnelMapper;

	/**
     * 查询信访相关人员信息
     * 
     * @param id 信访相关人员ID
     * @return 信访相关人员信息
     */
    @Override
	public PetitionPersonnel selectPetitionPersonnelById(Long id)
	{
	    return petitionPersonnelMapper.selectPetitionPersonnelById(id);
	}
	
	/**
     * 查询信访相关人员列表
     * 
     * @param petitionPersonnel 信访相关人员信息
     * @return 信访相关人员集合
     */
	@Override
	public List<PetitionPersonnel> selectPetitionPersonnelList(PetitionPersonnel petitionPersonnel)
	{
	    return petitionPersonnelMapper.selectPetitionPersonnelList(petitionPersonnel);
	}
	
    /**
     * 新增信访相关人员
     * 
     * @param petitionPersonnel 信访相关人员信息
     * @return 结果
     */
	@Override
	public int insertPetitionPersonnel(PetitionPersonnel petitionPersonnel)
	{
	    return petitionPersonnelMapper.insertPetitionPersonnel(petitionPersonnel);
	}
	
	/**
     * 修改信访相关人员
     * 
     * @param petitionPersonnel 信访相关人员信息
     * @return 结果
     */
	@Override
	public int updatePetitionPersonnel(PetitionPersonnel petitionPersonnel)
	{
	    return petitionPersonnelMapper.updatePetitionPersonnel(petitionPersonnel);
	}

	/**
     * 删除信访相关人员对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deletePetitionPersonnelByIds(String ids)
	{
		return petitionPersonnelMapper.deletePetitionPersonnelByIds(Convert.toStrArray(ids));
	}

	@Override
	public HashMap<Long, String> getPetitionPersonnelBingList(Long petitionId, String personnelType) {

		HashMap<Long, String> personMap = new HashMap<>();
		HashMap<String, Object> daoParam = new HashMap<>();
		daoParam.put("petitionId",petitionId);
		daoParam.put("personnelType",personnelType);
		List<PetitionPersonnel> bingList = petitionPersonnelMapper.getPetitionPersonnelBingList(daoParam);
		bingList.stream().forEach(bing ->{
			personMap.put(bing.getId(),bing.getName());
		});
		return personMap;
	}
}
