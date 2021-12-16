package com.mkst.umap.app.admin.service;

import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.workflow.domain.EventAuditRecord;
import com.mkst.umap.app.admin.api.bean.param.CarApproveParam;
import com.mkst.umap.app.admin.api.bean.result.car.CarApplyResult;
import com.mkst.umap.app.admin.api.bean.result.car.CarDetailResult;
import com.mkst.umap.app.admin.domain.CarApply;
import com.mkst.umap.app.admin.domain.MapLocation;
import com.mkst.umap.app.admin.dto.carApply.CarApplyInfoDto;

import java.util.List;

/**
 * 车辆申请管理 服务层
 * 
 * @author wangyong
 * @date 2020-07-20
 */
public interface ICarApplyService 
{
	/**
     * 查询车辆申请管理信息
     * 
     * @param carApplyId 车辆申请管理ID
     * @return 车辆申请管理信息
     */
	public CarApply selectCarApplyById(Long carApplyId);

	public CarDetailResult selectCarDetailById(Long carApplyId);

	List<Long> checkCarUseByTime(CarApply carApply);

	List<Long> checkDriverByTime(CarApply carApply);

	List<CarApply> selectCarApplyByStatus(CarApply carApply);

	List<CarApply> selectCarApplyByDriver(CarApply carApply);
	/**
     * 查询车辆申请管理列表
     * 
     * @param carApply 车辆申请管理信息
     * @return 车辆申请管理集合
     */
	public List<CarApply> selectCarApplyList1(CarApply carApply);

	public List<CarApply> selectCarApplyList(CarApply carApply);

	List<CarApplyInfoDto> selectCarApplyDtoList(CarApply carApply);

	List<CarApplyResult> selectResultList(CarApply carApply);

	public List<CarApplyResult> getResultList(List<CarApply> carApplyList);
	/**
     * 新增车辆申请管理
     * 
     * @param carApply 车辆申请管理信息
     * @return 结果
     */
	public int insertCarApply(CarApply carApply);

	Integer addSave(CarApply carApply, SysUser nowUser,MapLocation startPoint, MapLocation endPoint);
	
	/**
     * 修改车辆申请管理
     * 
     * @param carApply 车辆申请管理信息
     * @return 结果
     */
	public int updateCarApply(CarApply carApply);

	public int updateCarApplyFee(CarApply carApply);

	/**
     * 删除车辆申请管理信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteCarApplyByIds(String ids);

	int audit(Long id, String status, String reason);

	int auditByParam(CarApproveParam param);

	List<EventAuditRecord> selectAuditList(String applyType, Long applyId);

}
