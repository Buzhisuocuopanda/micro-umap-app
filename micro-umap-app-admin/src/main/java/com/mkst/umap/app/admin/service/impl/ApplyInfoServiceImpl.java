package com.mkst.umap.app.admin.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import com.mkst.mini.systemplus.common.shiro.utils.ShiroUtils;
import com.mkst.mini.systemplus.common.support.Convert;
import com.mkst.umap.app.admin.api.bean.param.backup.BackUpParam;
import com.mkst.umap.app.admin.api.bean.result.BackUpApplyCount;
import com.mkst.umap.app.admin.api.bean.result.NameCountResult;
import com.mkst.umap.app.admin.domain.ApplyInfo;
import com.mkst.umap.app.admin.domain.AuditRecord;
import com.mkst.umap.app.admin.domain.BackUpGuest;
import com.mkst.umap.app.admin.domain.vo.ApplyInfoVo;
import com.mkst.umap.app.admin.domain.vo.BackUpGuestVo;
import com.mkst.umap.app.admin.dto.apply.ApplyInfoDto;
import com.mkst.umap.app.admin.dto.apply.ApplyNumberDto;
import com.mkst.umap.app.admin.mapper.ApplyInfoMapper;
import com.mkst.umap.app.admin.mapper.AuditRecordMapper;
import com.mkst.umap.app.admin.service.IApplyInfoService;
import com.mkst.umap.app.admin.service.IAuditRecordService;
import com.mkst.umap.app.admin.service.IBackUpGuestService;
import com.mkst.umap.app.common.constant.MsgConstant;
import com.mkst.umap.app.common.enums.ApplyStatusEnum;
import com.mkst.umap.app.common.enums.ApproveStatusEnum;
import com.mkst.umap.app.common.enums.AuditRecordTypeEnum;
import com.mkst.umap.app.common.enums.AuditStatusEnum;

/**
 * 备勤间申请 服务层实现
 * 
 * @author lijinghui
 * @date 2020-06-17
 */
@Service
public class ApplyInfoServiceImpl implements IApplyInfoService 
{
	@Autowired
	private ApplyInfoMapper applyInfoMapper;

	@Autowired
	private IBackUpGuestService backUpGuestService;

	@Autowired
	private IAuditRecordService auditRecordService;
	@Autowired
	private AuditRecordMapper auditRecordMapper;
	/**
     * 查询备勤间申请信息
     * 
     * @param applyId 备勤间申请ID
     * @return 备勤间申请信息
     */
    @Override
	public ApplyInfo selectApplyInfoById(Long applyId)
	{
	    return applyInfoMapper.selectApplyInfoById(applyId);
	}

	@Override
    public ApplyInfoVo selectApplyVoById(Long applyId){
		return applyInfoMapper.selectApplyVoById(applyId);
	}
	
	/**
     * 查询备勤间申请列表
     * 
     * @param applyInfo 备勤间申请信息
     * @return 备勤间申请集合
     */
	@Override
	public List<ApplyInfo> selectApplyInfoList(ApplyInfo applyInfo)
	{
	    return applyInfoMapper.selectApplyInfoList(applyInfo);
	}

	@Override
	public List<ApplyInfo> selectApplyInfoListTask(ApplyInfo applyInfo){
		return applyInfoMapper.selectApplyInfoListTask(applyInfo);
	}

	/**
	 * 通过vo获取vo集合
	 * @param applyInfoDto
	 * @return
	 */
	@Override
	public List<ApplyInfoVo> selectApplyVo(ApplyInfoDto applyInfoDto){
		//通过applyId查询 必然只会查到一个
		List<ApplyInfoVo> voList = applyInfoMapper.selectApplyVo(applyInfoDto);

		for(ApplyInfoVo vo : voList){
			List<BackUpGuestVo> guests = backUpGuestService.selectGuestVoList(vo.getApplyId());
			vo.setPeopleNum(guests.size());
			vo.setBackUpGuests(guests);
			if(vo.getEndTime()!=null&&vo.getStartTime()!=null){
				vo.setDayNum(((vo.getEndTime().getTime() - vo.getStartTime().getTime()) / (60 * 60 * 24 * 1000)));
			}
		}
		return voList;
	}

	@Override
	public void detail(Long id, ModelMap mmap){

		ApplyInfoVo applyInfoVo = selectApplyVoById(id);
		List<BackUpGuestVo> guests = backUpGuestService.selectGuestVoList(id);
		applyInfoVo.setBackUpGuests(guests);
		applyInfoVo.setPeopleNum(guests.size());
		AuditRecord auditRecord = new AuditRecord();
		auditRecord.setApplyId(id);
		auditRecord.setApplyType(AuditRecordTypeEnum.BackUpAudit.getValue());
		List<AuditRecord> auditRecords = auditRecordService.selectAuditRecordList(auditRecord);

		mmap.put("auditRecords", auditRecords);
		mmap.put("applyInfoVo", applyInfoVo);

	}

	/**
	 * 通过申请单开始和结束时间获取 时间段类有预约的房间集合
	 * @param applyInfoDto
	 * @return
	 */
	@Override
	public List<Long> selectApplyRoomByDates(ApplyInfoDto applyInfoDto){
		return applyInfoMapper.selectApplyRoomByDates(applyInfoDto);
	}

    /**
     * 新增备勤间申请
     * 
     * @param applyInfo 备勤间申请信息
     * @return 结果
     */
	@Override
	public int insertApplyInfoWithGuests(ApplyInfo applyInfo)
	{
	    int rows = applyInfoMapper.insertApplyInfo(applyInfo);
	    //添加成功后 再新增使用人
	    if(rows>0){
			//批量sql注入，防止意外中断有的使用人增加成功有的失败
			List<BackUpGuest> list = applyInfo.getBackUpGuests();
			for(BackUpGuest backUpGuest : list){
				backUpGuest.setApplyId(applyInfo.getApplyId());
				backUpGuest.setCreateBy(applyInfo.getCreateBy());
			}
			backUpGuestService.batchBackUpGuest(list);
		}
		return rows;
	}

	@Override
	public int insertApply(ApplyInfo applyInfo)
	{
		int rows = applyInfoMapper.insertApplyInfo(applyInfo);
		return rows;
	}


	/**
     * 修改备勤间申请
     * 
     * @param applyInfo 备勤间申请信息
     * @return 结果
     */
	@Override
	public int updateApplyInfo(ApplyInfo applyInfo)
	{
	    return applyInfoMapper.updateApplyInfo(applyInfo);
	}

	/**
     * 删除备勤间申请对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteApplyInfoByIds(String ids)
	{
		return applyInfoMapper.deleteApplyInfoByIds(Convert.toStrArray(ids));
	}

	public int auditNew(ApplyInfo ai){
		return applyInfoMapper.updateApplyInfo(ai);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int audit(Long id, String status, String reason) {

		ApplyInfo applyInfo = selectApplyInfoById(id);

		if (!ApplyStatusEnum.Pending.getValue().equals(applyInfo.getApplyStatus())) {
			return 0;
		}

		//新增审核记录
		AuditRecord auditRecord = new AuditRecord(applyInfo.getApplyId(), AuditRecordTypeEnum.BackUpAudit.getValue(), status, "");
		auditRecord.setCreateBy(ShiroUtils.getLoginName());
		auditRecord.setUpdateBy(ShiroUtils.getLoginName());
		if (!reason.equals(MsgConstant.USER_AUDIT_NO_REASON_FLAG)) {
			auditRecord.setReason(reason);
		}
		auditRecordMapper.insertAuditRecord(auditRecord);

		if(AuditStatusEnum.EVENT_AUDIT_STATUS_PASS.getValue().toString().equals(status)){
			applyInfo.setApplyStatus(ApproveStatusEnum.SUCCESS.getValue());
		}else {
			applyInfo.setApplyStatus(ApproveStatusEnum.FAIL.getValue());
		}

		int row = this.updateApplyInfo(applyInfo);

		return row;
	}
	@Override
	public List<ApplyInfoDto> selectApplyInfoByStatus(ApplyInfo applyInfo){
		return applyInfoMapper.selectApplyInfoByStatus(applyInfo);
	}

	@Override
	public List<NameCountResult> selectDayCount(BackUpParam param){
		List<NameCountResult> result = applyInfoMapper.selectDayCount(param);
		result.stream().forEach(o -> o.setStatus(false));
		return result;
	}

	public List<ApplyInfo> selectApplyInfoListByMap(Map<String , Object> params ){

		return applyInfoMapper.selectApplyInfoListByMap(params);
	}
	
	@Override
	public List<ApplyNumberDto> countGroupbyRoomIdByTime(Date startTime) {
		return applyInfoMapper.countGroupbyRoomIdByTime(startTime);
	}
	
	/**
	 * 获取预约总数
	 */
	@Override
	public int countApplyNumber() {
		return applyInfoMapper.countApplyNumber();
	}
	/**
	 * 获取今日预约数量
	 */
	@Override
	public int countApplyNumberByDay() {
		return applyInfoMapper.countApplyNumberByDay();
	}
	
	@Override
	public List<BackUpApplyCount> countApplyNumberByUserAndDate(ApplyInfo applyInfo) {
		return applyInfoMapper.countApplyNumberByUserAndDate(applyInfo);
	}
}
