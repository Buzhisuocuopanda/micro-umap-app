package com.mkst.umap.app.admin.service;

import com.mkst.umap.app.admin.domain.Report;
import java.util.List;

/**
 * 随手拍/公益举报 服务层
 * 
 * @author wangyong
 * @date 2020-08-27
 */
public interface IReportService 
{
	/**
     * 查询随手拍/公益举报信息
     * 
     * @param id 随手拍/公益举报ID
     * @return 随手拍/公益举报信息
     */
	public Report selectReportById(Long id);
	
	/**
     * 查询随手拍/公益举报列表
     * 
     * @param report 随手拍/公益举报信息
     * @return 随手拍/公益举报集合
     */
	public List<Report> selectReportList(Report report);
	
	/**
     * 新增随手拍/公益举报
     * 
     * @param report 随手拍/公益举报信息
     * @return 结果
     */
	public int insertReport(Report report);
	
	/**
     * 修改随手拍/公益举报
     * 
     * @param report 随手拍/公益举报信息
     * @return 结果
     */
	public int updateReport(Report report);
		
	/**
     * 删除随手拍/公益举报信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteReportByIds(String ids);

	List<String> getFileBind(Long id);

    int reply(Long id, String content, String loginName);
}
