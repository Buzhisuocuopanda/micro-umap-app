package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.api.bean.param.canteen.CanteenManageParam;
import com.mkst.umap.app.admin.api.bean.result.NameCountResult;
import com.mkst.umap.app.admin.api.bean.result.canteen.CanteenDetailResult;
import com.mkst.umap.app.admin.api.bean.result.canteen.CanteenResult;
import com.mkst.umap.app.admin.api.bean.result.canteen.MealTypeCountResult;
import com.mkst.umap.app.admin.domain.CanteenManage;
import com.mkst.umap.app.admin.statistics.AnalysisCountResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 食堂申请 数据层
 * 
 * @author wangyong
 * @date 2020-07-20
 */
public interface CanteenManageMapper 
{
	/**
     * 查询食堂申请信息
     * 
     * @param canteenApplyId 食堂申请ID
     * @return 食堂申请信息
     */
	public CanteenManage selectCanteenManageById(Long canteenApplyId);

	/**
	 * 获取 餐次 包厢  申请所有信息 通过id
	 * @param canteenApplyId
	 * @return
	 */
	CanteenDetailResult selectCanteenManageAllById(Long canteenApplyId);

	/**
     * 查询食堂申请列表
     * 
     * @param canteenManage 食堂申请信息
     * @return 食堂申请集合
     */
	public List<CanteenManage> selectCanteenManageList(CanteenManage canteenManage);

	/**
	 * 通过包厢id查询食堂预约集合
	 * @param map
	 * @return
	 */
	List<CanteenManage> selectCanteenListByBoxId(@Param("params") Map<String, Object> map);

	/**
	 * 通过日期查询食堂返回信息列表
	 * @param canteenManage
	 * @return
	 */
	List<CanteenResult> selectCanteenResultByDate(CanteenManage canteenManage);


	List<CanteenResult> selectCanteenResultByStatus(CanteenManage canteenManage);

	List<CanteenResult> selectCanteenResultByCancel(CanteenManage canteenManage);

	/**
	 * 获取选定日期的状态为未审核和通过的预约总数
	 * @param date
	 * @return
	 */
	Integer selectCanteenManageCountByDateAndStatusEq01(String date);

	/**
     * 新增食堂申请
     * 
     * @param canteenManage 食堂申请信息
     * @return 结果
     */
	public int insertCanteenManage(CanteenManage canteenManage);
	
	/**
     * 修改食堂申请
     * 
     * @param canteenManage 食堂申请信息
     * @return 结果
     */
	public int updateCanteenManage(CanteenManage canteenManage);
	
	/**
     * 删除食堂申请
     * 
     * @param canteenApplyId 食堂申请ID
     * @return 结果
     */
	public int deleteCanteenManageById(Long canteenApplyId);
	
	/**
     * 批量删除食堂申请
     * 
     * @param canteenApplyIds 需要删除的数据ID
     * @return 结果
     */
	public int deleteCanteenManageByIds(String[] canteenApplyIds);

    List<MealTypeCountResult> countByMealType(CanteenManageParam canteenManageParam);

    List<NameCountResult> selectDayCount(CanteenManageParam param);

    List<CanteenResult> selectAuditListByParam(CanteenManageParam param);

    Integer selectTotalByDay(@Param("startTime") String startTime,@Param("endTime") String endTime);

	List<AnalysisCountResult> analysisList(@Param("startDay") String startDay, @Param("endDay") String endDay);

	Long selectTotal();

}