package com.mkst.umap.app.admin.service;

import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.workflow.domain.EventAuditRecord;
import com.mkst.umap.app.admin.api.bean.param.ApproveParam;
import com.mkst.umap.app.admin.api.bean.param.canteen.CanteenManageParam;
import com.mkst.umap.app.admin.api.bean.result.NameCountResult;
import com.mkst.umap.app.admin.api.bean.result.canteen.CanteenDetailResult;
import com.mkst.umap.app.admin.api.bean.result.canteen.CanteenResult;
import com.mkst.umap.app.admin.api.bean.result.canteen.MealTypeCountResult;
import com.mkst.umap.app.admin.domain.CanteenManage;

import java.util.List;
import java.util.Map;

/**
 * 食堂申请 服务层
 * 
 * @author wangyong
 * @date 2020-07-20
 */
public interface ICanteenManageService 
{
	/**
     * 查询食堂申请信息
     * 
     * @param canteenApplyId 食堂申请ID
     * @return 食堂申请信息
     */
	public CanteenManage selectCanteenManageById(Long canteenApplyId);


	CanteenDetailResult selectCanteenManageAllById(Long canteenApplyId);
	/**
     * 查询食堂申请列表
     * 
     * @param canteenManage 食堂申请信息
     * @return 食堂申请集合
     */
	public List<CanteenManage> selectCanteenManageList(CanteenManage canteenManage);

	/**
	 * 查询预约餐次集合
	 * @param map
	 * @return
	 */
	List<CanteenManage> selectCanteenListByBoxId(Map<String, Object> map);

	/**
	 * 获取包厢使用情况
	 * @param canteenManage
	 * @return
	 */
	List<CanteenResult> selectCanteenResultByDate(CanteenManage canteenManage);

	List<MealTypeCountResult> countByMealType(CanteenManageParam canteenManageParam);
	/**
	 *
	 * @param canteenManage
	 * @return
	 */
	List<CanteenResult> selectCanteenResultByStatus(CanteenManage canteenManage);


	List<CanteenResult> selectCanteenResultByCancel(CanteenManage canteenManage);

	/**
	 * 查询指定日期内  状态为待审核和通过审核的数量
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

	public String auditUpdateSuccess(SysUser sysUser, Long applyId);
		
	/**
     * 删除食堂申请信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteCanteenManageByIds(String ids);

	public String audit(Long id, String status);

    List<NameCountResult> selectDayCount(CanteenManageParam param);

    int auditByParam(ApproveParam param);

	List<EventAuditRecord> selectAuditList(Long applyId);

    List<CanteenResult> selectApplyAuditListByParam(CanteenManageParam param);

}
