package com.mkst.umap.app.admin.service;

import com.mkst.umap.app.admin.api.bean.param.backup.BackUpParam;
import com.mkst.umap.app.admin.api.bean.param.meeting.MeetingParam;
import com.mkst.umap.app.admin.api.bean.result.NameCountResult;
import com.mkst.umap.app.admin.domain.ApplyInfo;
import com.mkst.umap.app.admin.domain.vo.ApplyInfoVo;
import com.mkst.umap.app.admin.dto.apply.ApplyInfoDto;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.Map;

/**
 * 备勤间申请 服务层
 * 
 * @author lijinghui
 * @date 2020-06-17
 */
public interface IApplyInfoService 
{
	/**
     * 查询备勤间申请信息
     * 
     * @param applyId 备勤间申请ID
     * @return 备勤间申请信息
     */
	ApplyInfo selectApplyInfoById(Long applyId);

	/**
	 * 通过id查询单个 备勤间视图信息
	 * @param applyId
	 * @return
	 */
	public ApplyInfoVo selectApplyVoById(Long applyId);

	/**
     * 查询备勤间申请列表
     * 
     * @param applyInfo 备勤间申请信息
     * @return 备勤间申请集合
     */
	List<ApplyInfo> selectApplyInfoList(ApplyInfo applyInfo);


	/**
	 * 定时任务查询条件
	 * @param applyInfo
	 * @return
	 */
	List<ApplyInfo> selectApplyInfoListTask(ApplyInfo applyInfo);
	/**
	 * 通过vo获取vo集合
	 * @param applyInfoDto
	 * @return
	 */
	List<ApplyInfoVo> selectApplyVo(ApplyInfoDto applyInfoDto);


	void detail(Long id, ModelMap mmap);


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
	int insertApplyInfoWithGuests(ApplyInfo applyInfo);

	/**
	 * 申请备勤间
	 * @param applyInfo
	 * @return
	 */
	int insertApply(ApplyInfo applyInfo);
	
	/**
     * 修改备勤间申请
     * 
     * @param applyInfo 备勤间申请信息
     * @return 结果
     */
	int updateApplyInfo(ApplyInfo applyInfo);
		
	/**
     * 删除备勤间申请信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteApplyInfoByIds(String ids);

	int audit(Long id, String status, String reason);

	int auditNew(ApplyInfo ai);

	List<ApplyInfoDto> selectApplyInfoByStatus(ApplyInfo applyInfo);

	List<NameCountResult> selectDayCount(BackUpParam param);

	List<ApplyInfo> selectApplyInfoListByMap(Map<String , Object> params );

}
