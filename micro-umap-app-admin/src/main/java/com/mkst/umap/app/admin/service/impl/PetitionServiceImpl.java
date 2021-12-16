package com.mkst.umap.app.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.mkst.mini.systemplus.common.support.Convert;
import com.mkst.mini.systemplus.system.domain.SysFileUpload;
import com.mkst.mini.systemplus.util.FileUploadExtendUtils;
import com.mkst.umap.app.admin.domain.Petition;
import com.mkst.umap.app.admin.domain.PetitionMatter;
import com.mkst.umap.app.admin.mapper.PetitionMapper;
import com.mkst.umap.app.admin.service.IPetitionMatterService;
import com.mkst.umap.app.admin.service.IPetitionPersonnelService;
import com.mkst.umap.app.admin.service.IPetitionService;
import com.mkst.umap.app.common.enums.BusinessTypeEnum;
import com.mkst.umap.app.common.enums.PetitionCivilMatterTypeEnum;
import com.mkst.umap.app.common.enums.PetitionPersonnelTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 信访主体 服务层实现
 * 
 * @author wangyong
 * @date 2020-08-25
 */
@Service
public class PetitionServiceImpl implements IPetitionService 
{
	@Autowired
	private PetitionMapper petitionMapper;

	@Autowired
	private IPetitionPersonnelService personnelService;

	@Autowired
	private IPetitionMatterService matterService;

	/**
     * 查询信访主体信息
     * 
     * @param id 信访主体ID
     * @return 信访主体信息
     */
    @Override
	public Petition selectPetitionById(Long id)
	{
	    return petitionMapper.selectPetitionById(id);
	}
	
	/**
     * 查询信访主体列表
     * 
     * @param petition 信访主体信息
     * @return 信访主体集合
     */
	@Override
	public List<Petition> selectPetitionList(Petition petition)
	{

	    return petitionMapper.selectPetitionList(petition);
	}
	
    /**
     * 新增信访主体
     * 
     * @param petition 信访主体信息
     * @return 结果
     */
	@Override
	public int insertPetition(Petition petition)
	{
	    return petitionMapper.insertPetition(petition);
	}
	
	/**
     * 修改信访主体
     * 
     * @param petition 信访主体信息
     * @return 结果
     */
	@Override
	public int updatePetition(Petition petition)
	{
	    return petitionMapper.updatePetition(petition);
	}

	/**
     * 删除信访主体对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deletePetitionByIds(String ids)
	{
		return petitionMapper.deletePetitionByIds(Convert.toStrArray(ids));
	}

	@Override
	public List<String> getFileBind(Long id) {

		ArrayList<String> resultList = new ArrayList<>();

		Petition petition = this.selectPetitionById(id);
		if (petition == null){
			return resultList;
		}

		List<SysFileUpload> uploadList = FileUploadExtendUtils.findFileUpload(id.toString(), BusinessTypeEnum.UMAP_PETITION.getValue());

		if (CollectionUtil.isEmpty(uploadList)){
			return resultList;
		}

		uploadList.stream().forEach(fileUpload ->{
			fileUpload.getFileEntityList().stream().forEach(file -> {
				resultList.add(file.getFilePath());
			});
		});

		return resultList;
	}

	@Override
	public Map<String, Map<Long, String>> getPersonnelBind(Long petitionId) {

		HashMap<String, Map<Long, String>> resultMap = new HashMap<>();

		Arrays.asList(PetitionPersonnelTypeEnum.values()).stream().forEach(personnelTypeEnum ->{
			String personnelType = personnelTypeEnum.getValue();
			Map<Long, String> personMap = personnelService.getPetitionPersonnelBingList(petitionId, personnelType);
			if(CollectionUtil.isNotEmpty(personMap)){
				resultMap.put(personnelType,personMap);
			}
		});
		return resultMap;
	}

	@Override
	public Map<String, Long> getMatterInfo(Long petitionId) {

		HashMap<String, Long> resultMap = new HashMap<>();

		Arrays.asList(PetitionCivilMatterTypeEnum.values()).stream().forEach(matterTypeEnum ->{
			String matterType = matterTypeEnum.getValue();
			PetitionMatter matter = matterService.getPetitionMatterBing(petitionId, matterType);
			if (BeanUtil.isNotEmpty(matter)){
				resultMap.put(matterType,matter.getId());
			}
		});
		return resultMap;
	}

	@Override
	public String getUserName(){
		return petitionMapper.getUserName();
	}


}
