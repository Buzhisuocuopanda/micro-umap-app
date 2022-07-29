package com.mkst.umap.app.admin.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mkst.umap.app.admin.api.bean.param.backup.BackUpParam;
import com.mkst.umap.app.admin.api.bean.result.BackUpApplyCount;
import com.mkst.umap.app.admin.api.bean.result.NameCountResult;
import com.mkst.umap.app.admin.domain.ApplyInfo;
import com.mkst.umap.app.admin.domain.vo.ApplyInfoVo;
import com.mkst.umap.app.admin.dto.apply.ApplyInfoDto;
import com.mkst.umap.app.admin.dto.apply.ApplyNumberDto;
import org.apache.ibatis.annotations.Param;

/**
 * 备勤间申请 数据层
 * 
 * @author lijinghui
 * @date 2020-06-17
 */
public interface ApplyInfoMapper 
{
	/**
     * 查询备勤间申请信息
     * 
     * @param applyId 备勤间申请ID
     * @return 备勤间申请信息
     */
	ApplyInfo selectApplyInfoById(Long applyId);

	ApplyInfoVo selectApplyVoById(Long applyId);

	/**
     * 查询备勤间申请列表
     * 
     * @param applyInfo 备勤间申请信息
     * @return 备勤间申请集合
     */
	List<ApplyInfo> selectApplyInfoList(ApplyInfo applyInfo);


	List<ApplyInfo> selectApplyInfoListTask(ApplyInfo applyInfo);

	/**
	 * 通过vo获取vo视图集合
	 * @param applyInfoDto
	 * @return
	 */
	List<ApplyInfoVo> selectApplyVo(ApplyInfoDto applyInfoDto);

	/**
	 * 通过申请单开始和结束时间获取 时间段类有预约的房间集合
	 * @param applyInfoDto
	 * @return
	 */
	List<Long> selectApplyRoomByDates(ApplyInfoDto applyInfoDto);

	/**
     * 新增备勤间申请
     * 
     * @param applyInfo 备勤间申请信息
     * @return 结果
     */
	int insertApplyInfo(ApplyInfo applyInfo);
	
	/**
     * 修改备勤间申请
     * 
     * @param applyInfo 备勤间申请信息
     * @return 结果
     */
	int updateApplyInfo(ApplyInfo applyInfo);
	
	/**
     * 删除备勤间申请
     * 
     * @param applyId 备勤间申请ID
     * @return 结果
     */
	int deleteApplyInfoById(Integer applyId);
	
	/**
     * 批量删除备勤间申请
     * 
     * @param applyIds 需要删除的数据ID
     * @return 结果
     */
	int deleteApplyInfoByIds(String[] applyIds);

	List<ApplyInfoDto> selectApplyInfoByStatus(ApplyInfo applyInfo);

	List<NameCountResult> selectDayCount(BackUpParam param);

	List<ApplyInfo> selectApplyInfoListByMap(Map<String , Object> params);
	
	List<ApplyNumberDto> countGroupbyRoomIdByTime(Date startTime);
	
	/**
	 * 获取预约总数和今日预约数量
	 */
	int countApplyNumber();
	/**
	 * 获取今日预约数量
	 */
	int countApplyNumberByDay(Date date);
	/**
	 * 获取今日该性别预约数量
	 */
	int countApplySexNumberByDay(@Param("startTime")Date startTime, @Param("userSex")String userSex);
	/**
	 * 查询当日非该性别预约房间的总位数
	 */
	int countApplySexRoomNumByDay(@Param("startTime")Date startTime, @Param("userSex")String userSex);
	List<BackUpApplyCount> countApplyNumberByUserAndDate(ApplyInfo applyInfo);

	/**
	 * 获取指定日期使用过的房间
	 */
	List<String> selectApplyUseRoomList(Date date);
}