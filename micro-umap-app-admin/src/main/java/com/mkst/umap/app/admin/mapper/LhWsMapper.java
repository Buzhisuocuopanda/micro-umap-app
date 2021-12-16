package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.api.bean.param.lhBook.LhWsParam;
import com.mkst.umap.app.admin.domain.LhWs;
import java.util.List;	

/**
 * 龙华文书同步 数据层
 * 
 * @author wangyong
 * @date 2020-09-15
 */
public interface LhWsMapper 
{
	/**
     * 查询龙华文书同步信息
     * 
     * @param id 龙华文书同步ID
     * @return 龙华文书同步信息
     */
	public LhWs selectLhWsById(Long id);
	
	/**
     * 查询龙华文书同步列表
     * 
     * @param lhWs 龙华文书同步信息
     * @return 龙华文书同步集合
     */
	public List<LhWs> selectLhWsList(LhWs lhWs);

	List<LhWs> selectLhWsListByAreaAndType(LhWsParam param);
	/**
     * 新增龙华文书同步
     * 
     * @param lhWs 龙华文书同步信息
     * @return 结果
     */
	public int insertLhWs(LhWs lhWs);
	
	/**
     * 修改龙华文书同步
     * 
     * @param lhWs 龙华文书同步信息
     * @return 结果
     */
	public int updateLhWs(LhWs lhWs);
	
	/**
     * 删除龙华文书同步
     * 
     * @param id 龙华文书同步ID
     * @return 结果
     */
	public int deleteLhWsById(Long id);
	
	/**
     * 批量删除龙华文书同步
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteLhWsByIds(String[] ids);
	
}