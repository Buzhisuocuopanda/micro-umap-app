package com.mkst.umap.app.admin.service;

import com.mkst.umap.app.admin.domain.PetitionMatterBind;
import java.util.List;

/**
 * 信访民行申诉与事项连接 服务层
 * 
 * @author wangyong
 * @date 2020-08-25
 */
public interface IPetitionMatterBindService 
{
	/**
     * 查询信访民行申诉与事项连接信息
     * 
     * @param id 信访民行申诉与事项连接ID
     * @return 信访民行申诉与事项连接信息
     */
	public PetitionMatterBind selectPetitionMatterBindById(Long id);
	
	/**
     * 查询信访民行申诉与事项连接列表
     * 
     * @param petitionMatterBind 信访民行申诉与事项连接信息
     * @return 信访民行申诉与事项连接集合
     */
	public List<PetitionMatterBind> selectPetitionMatterBindList(PetitionMatterBind petitionMatterBind);
	
	/**
     * 新增信访民行申诉与事项连接
     * 
     * @param petitionMatterBind 信访民行申诉与事项连接信息
     * @return 结果
     */
	public int insertPetitionMatterBind(PetitionMatterBind petitionMatterBind);
	
	/**
     * 修改信访民行申诉与事项连接
     * 
     * @param petitionMatterBind 信访民行申诉与事项连接信息
     * @return 结果
     */
	public int updatePetitionMatterBind(PetitionMatterBind petitionMatterBind);
		
	/**
     * 删除信访民行申诉与事项连接信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deletePetitionMatterBindByIds(String ids);

    int insert(Long petitionId, String matterIds, Long userId);
}
