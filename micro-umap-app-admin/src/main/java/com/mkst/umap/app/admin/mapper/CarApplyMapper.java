package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.api.bean.result.car.CarDetailResult;
import com.mkst.umap.app.admin.domain.CarApply;
import com.mkst.umap.app.admin.dto.carApply.CarApplyInfoDto;
import com.mkst.umap.app.admin.statistics.AnalysisCountResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 车辆申请管理 数据层
 * 
 * @author wangyong
 * @date 2020-07-20
 */
public interface CarApplyMapper 
{
	/**
     * 查询车辆申请管理信息
     * 
     * @param carApplyId 车辆申请管理ID
     * @return 车辆申请管理信息
     */
	public CarApply selectCarApplyById(Long carApplyId);

	public CarDetailResult selectCarDetailById(Long carApplyId);

	/**
	 * 检查预约的开始时间和结束时间段 是否存在其他预约
	 * @param carApply
	 * @return
	 */
	List<Long> checkCarUseByTime(CarApply carApply);

	List<Long> checkDriverByTime(CarApply carApply);
	/**
	 * 通过审核状态 开始结束时间 申请人查询list
	 * @param carApply
	 * @return
	 */
	List<CarApply> selectCarApplyByStatus(CarApply carApply);

	/**
	 * 通过 是否需要司机  司机id  司机接单状态 开始结束时间 和审核状态查询list集合
	 * @param carApply
	 * @return
	 */
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
	/**
     * 新增车辆申请管理
     * 
     * @param carApply 车辆申请管理信息
     * @return 结果
     */
	public int insertCarApply(CarApply carApply);
	
	/**
     * 修改车辆申请管理
     * 
     * @param carApply 车辆申请管理信息
     * @return 结果
     */
	public int updateCarApply(CarApply carApply);

	public int updateCarApplyFee(CarApply carApply);
	
	/**
     * 删除车辆申请管理
     * 
     * @param carApplyId 车辆申请管理ID
     * @return 结果
     */
	public int deleteCarApplyById(Long carApplyId);
	
	/**
     * 批量删除车辆申请管理
     * 
     * @param carApplyIds 需要删除的数据ID
     * @return 结果
     */
	public int deleteCarApplyByIds(String[] carApplyIds);

	//public Integer selectCarTodayOrder();

	//public Integer selectCarTotalOrder();

	Integer selectTotalByDay(@Param("startTime") String startTime,@Param("endTime") String endTime);

	Long selectTotal();

	List<AnalysisCountResult> selectCarOrderList(@Param("startDay") String startDay, @Param("endDay") String endDay);

}