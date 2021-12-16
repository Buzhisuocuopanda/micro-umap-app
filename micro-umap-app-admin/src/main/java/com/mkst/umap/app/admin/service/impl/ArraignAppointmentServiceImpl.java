package com.mkst.umap.app.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileReader;
import com.alibaba.fastjson.JSONArray;
import com.gexin.fastjson.JSONObject;
import com.mkst.mini.systemplus.common.annotation.DataScope;
import com.mkst.mini.systemplus.common.shiro.utils.ShiroUtils;
import com.mkst.mini.systemplus.common.support.Convert;
import com.mkst.mini.systemplus.common.utils.DateUtils;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.mini.systemplus.util.HolidayUtil;
import com.mkst.mini.systemplus.util.SysConfigUtil;
import com.mkst.umap.app.admin.api.bean.param.ArraignAppointmentParam;
import com.mkst.umap.app.admin.api.bean.param.SelectProcustorParam;
import com.mkst.umap.app.admin.api.bean.result.NameCountResult;
import com.mkst.umap.app.admin.api.bean.result.arraign.TimeApplyResult;
import com.mkst.umap.app.admin.domain.*;
import com.mkst.umap.app.admin.dto.arraign.*;
import com.mkst.umap.app.admin.mapper.*;
import com.mkst.umap.app.admin.service.IArraignAppointmentService;
import com.mkst.umap.app.admin.service.IArraignRoomService;
import com.mkst.umap.app.common.constant.KeyConstant;
import com.mkst.umap.app.common.constant.MsgConstant;
import com.mkst.umap.app.common.enums.AuditRecordTypeEnum;
import com.mkst.umap.app.common.enums.RoomColorEnum;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 提审预约 服务层实现
 *
 * @author lijinghui
 * @date 2020-06-11
 */
@Service
@Slf4j
public class ArraignAppointmentServiceImpl implements IArraignAppointmentService {
	@Autowired
	private ArraignAppointmentMapper arraignAppointmentMapper;
	@Autowired
	private ArraignRoomScheduleMapper arraignRoomScheduleMapper;
	@Autowired
	private AuditRecordMapper auditRecordMapper;
	@Autowired
	private ISysUserService userService;
	@Autowired
	private IArraignRoomService roomService;
	@Value("${systemplus.profile}")
	private String profileDir;
	@Autowired
	private ArraignAppointmentOldMapper arraignAppointmentOldMapper;
	@Autowired
	private SysUserOldMapper sysUserOldMapper;

	/**
	 * 查询提审预约信息
	 *
	 * @param id 提审预约ID
	 * @return 提审预约信息
	 */
	@Override
	public ArraignAppointment selectArraignAppointmentById(Long id) {
		return arraignAppointmentMapper.selectArraignAppointmentById(id);
	}


	/**提审预约当日统计 通过roomId*/
	@Override
	public List<ArraignAppointmentTotalDto> selectTotalByRoomId(ArraignAppointmentTotalDto dto){
		return arraignAppointmentMapper.selectTotalByRoomId(dto);
	}

	/**提审预约 当天 通过犯罪类型*/
	@Override
	public List<ArraignAppointmentTotalDto> selectTotalByCriminalType(ArraignAppointmentTotalDto dto){
		return arraignAppointmentMapper.selectTotalByCriminalType(dto);
	}

	@Override
	public List<CountDto> selectDeptFive30Day(){
		return arraignAppointmentMapper.selectDeptFive30Day();
	}

	@Override
	public Integer selectTotalByTime(ArraignAppointmentTotalDto dto){
		return arraignAppointmentMapper.selectTotalByTime( dto);
	}

	@Override
	@DataScope(tableAlias = "sd",userAlias = "su")
	public List<ReportDto> selectReport(ReportDto dto){
		return arraignAppointmentMapper.selectReport(dto);
	}

	@Override
	@DataScope(tableAlias = "sd",userAlias = "su")
	public List<CriminalTypeResultDto> selectGroupByCriminalType(ReportDto dto){
		return arraignAppointmentMapper.selectGroupByCriminalType(dto);
	}

	@Override
	@DataScope(tableAlias = "sd",userAlias = "su")
	public List<CriminalTypeResultDto> select30Day(ReportDto dto){
		return arraignAppointmentMapper.select30Day(dto);
	}

	@Override
	public List<String> selectActiveUsers(String date){
		return arraignAppointmentMapper.selectActiveUsers(date);
	}

	@Override
	@DataScope(tableAlias = "sd",userAlias = "su")
	public List<CriminalTypeResultDto> selectSeasonGroupByCriminalType(ReportDto dto){
		return arraignAppointmentMapper.selectSeasonGroupByCriminalType(dto);
	}

	@Override
	@DataScope(tableAlias = "sd",userAlias = "su")
	public List<ReportDto> selectSeasonReport(ReportDto dto){
		return arraignAppointmentMapper.selectSeasonReport(dto);
	}

	/**
	 * 查询提审预约列表
	 *
	 * @param arraignAppointment 提审预约信息
	 * @return 提审预约集合
	 */
	@Override
	public List<ArraignAppointment> selectArraignAppointmentList(ArraignAppointment arraignAppointment) {
		return arraignAppointmentMapper.selectArraignAppointmentList(arraignAppointment);
	}

	@Override
	public Integer selectArraignAppointmentAuditCount() {
		return arraignAppointmentMapper.selectArraignAppointmentAuditCount();
	}

	@Override
	public List<ArraignAppointment> selectArraignAppointmentListByDto(ArraignAppointmentTotalDto dto){
		return arraignAppointmentMapper.selectArraignAppointmentListByDto(dto);
	}

	/**
	 * 新增提审预约
	 *
	 * @param arraignAppointment 提审预约信息
	 * @return 结果
	 */
	@Override
	public int insertArraignAppointment(ArraignAppointment arraignAppointment) {
		if (arraignAppointment.getCreateBy().isEmpty()) {
			arraignAppointment.setCreateBy(ShiroUtils.getLoginName());
		}
		Integer numOrder = arraignAppointmentMapper.selectMaxNumOrderByDay(arraignAppointment.getStartTime() , KeyConstant.EVENT_AUDIT_STATUS_PASS,arraignAppointment.getTimePie());
		if(numOrder == null){
			arraignAppointment.setNumOrder(1);
		}else{
			arraignAppointment.setNumOrder(numOrder.intValue()+1);
		}
		return arraignAppointmentMapper.insertArraignAppointment(arraignAppointment);
	}

	/**
	 * 修改提审预约
	 *
	 * @param arraignAppointment 提审预约信息
	 * @return 结果
	 */
	@Override
	public int updateArraignAppointment(ArraignAppointment arraignAppointment) {
		if (arraignAppointment.getUpdateBy() == null || arraignAppointment.getUpdateBy().isEmpty()) {
			arraignAppointment.setUpdateBy(ShiroUtils.getLoginName());
		}
		return arraignAppointmentMapper.updateArraignAppointment(arraignAppointment);
	}

	@Override
	public int updateAppointment(ArraignAppointment arraignAppointment) {
		if (arraignAppointment.getUpdateBy() == null || arraignAppointment.getUpdateBy().isEmpty()) {
			arraignAppointment.setUpdateBy(ShiroUtils.getLoginName());
		}
		return arraignAppointmentMapper.updateArraignAppointment(arraignAppointment);
	}


	@Override
	public int updateArraignAppointmentStatus(ArraignAppointment arraignAppointment){
		return arraignAppointmentMapper.updateArraignAppointment(arraignAppointment);
	}

	/**
	 * 删除提审预约对象
	 *
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int deleteArraignAppointmentByIds(String ids) {
		return arraignAppointmentMapper.deleteArraignAppointmentByIds(Convert.toStrArray(ids));
	}

	/**
	 * 批量更新提审申请状态，也可单个更新
	 *
	 * @param ids 需要改变的数据ID
	 * @return 结果
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int updateStatusByIds(String ids, String applyType, String status, String reason, String nowOperator) {
		String[] idArr = Convert.toStrArray(ids);

		if (idArr.length == 0) {
			return 0;
		}


		for (String id : idArr) {
			ArraignAppointment appointment = arraignAppointmentMapper.selectArraignAppointmentById(Long.valueOf(id));
			if(appointment == null){
				continue;
			}
			appointment.setStatus(status);
			appointment.setUpdateBy(nowOperator);
			appointment.setUpdateTime(new Date());
			appointment.setId(Long.valueOf(id));
			//更新排序
			if (status.equals(KeyConstant.EVENT_AUDIT_STATUS_PASS)) {
				Integer numOrder = arraignAppointmentMapper.selectMaxNumOrderByDay(appointment.getStartTime() , KeyConstant.EVENT_AUDIT_STATUS_PASS,appointment.getTimePie());
				if(numOrder == null){
					numOrder = 0;
				}
				appointment.setNumOrder(numOrder+1);
			}
			this.updateArraignAppointment(appointment);
			ArraignAppointment appointmentGetSchedule = this.selectArraignAppointmentById(Long.valueOf(id));
			//若是驳回请求，则需要删除排班表
			if (status.equals(KeyConstant.EVENT_AUDIT_STATUS_FAIL)) {
				arraignRoomScheduleMapper.deleteArraignRoomScheduleById(appointmentGetSchedule.getScheduleId());
			}
			//新增审核记录
			AuditRecord auditRecord = new AuditRecord(appointment.getId(), applyType, appointment.getStatus(), reason);
			auditRecord.setCreateBy(nowOperator);
			auditRecordMapper.insertAuditRecord(auditRecord);
		}
		return idArr.length;
	}


	/**
	 * @return java.util.List<com.mkst.umap.app.admin.dto.arraign.AppointmentDetailDto>
	 * @Author wangyong
	 * @Description 获取预约的详细信息
	 * @Date 14:51 2020-07-03
	 * @Param [appointmentDetailDto]
	 */
	@Override
	public List<AppointmentDetailDto> getAppointmentDetailList(AppointmentDetailDto appointmentDetailDto) {
		return arraignAppointmentMapper.selectAppointmentDetail(appointmentDetailDto);
	}

	@Override
	public AppointmentDetailDto getAppointmentDetailById(Long id) {

		AppointmentDetailDto appointmentDetailDto = new AppointmentDetailDto();
		appointmentDetailDto.setId(id);
		List<AppointmentDetailDto> appointmentDetailList = this.getAppointmentDetailList(appointmentDetailDto);

		if (appointmentDetailList.isEmpty()) {
			return null;
		}

		return appointmentDetailList.get(0);
	}

	@Override
	public List<ArraignAuditDto> selectArraignAppointmentAuditList(ArraignAuditDto arraignAuditDto) {
		return arraignAppointmentMapper.getArraignAuditDtoList(arraignAuditDto);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int audit(Long id, String status, String reason) {

		ArraignAppointment appointment = selectArraignAppointmentById(id);

		if (
				!appointment.getStatus().equals(KeyConstant.EVENT_AUDIT_STATUS_WAIT)
				&& !appointment.getStatus().equals("5")
		) {
			return 0;
		}
		//新增审核记录
		AuditRecord auditRecord = new AuditRecord(appointment.getId(), AuditRecordTypeEnum.ArraignAudit.getValue(), status, "");
		auditRecord.setCreateBy(ShiroUtils.getLoginName());
		auditRecord.setUpdateBy(ShiroUtils.getLoginName());
		if (!reason.equals(MsgConstant.USER_AUDIT_NO_REASON_FLAG)) {
			auditRecord.setReason(reason);
		}
		auditRecordMapper.insertAuditRecord(auditRecord);

		appointment.setStatus(status);

		int row = this.updateArraignAppointment(appointment);

		if (status.equals(KeyConstant.EVENT_AUDIT_STATUS_FAIL)) {
			arraignRoomScheduleMapper.deleteArraignRoomScheduleById(appointment.getScheduleId());
		}
		return row;
	}

	@Override
	public List<NameCountResult> selectDayCount(ArraignAppointmentParam param) {
		return arraignAppointmentMapper.selectDayCount(param);
	}

	@Override
	public List<NameCountResult> getAppointmentCalendar(Long userId){
		int limitDayCount = Integer.parseInt(SysConfigUtil.getKey(KeyConstant.ARRAIGN_APPOINTMENT_LIMIT_DAY));
		String openTime = SysConfigUtil.getKey(KeyConstant.ARRAIGN_APPOINTMENT_OPEN_TIME_KEY);
		//是否忽略假日
		String ignoreHolidayStr = SysConfigUtil.getKey(KeyConstant.ARRAIGN_APPOINTMENT_IGNORE_HOLIDAY_KEY);
		boolean ignoreHoliday = "1".equals(ignoreHolidayStr);
		List<NameCountResult> result  = new ArrayList<>();
		Date date = new Date();
		for(int i = 1 ; i <= limitDayCount ; date = DateUtil.offsetDay(date , 1)){
			//如果是最后一天 校验是否到了开放时间
			date = DateUtil.parse(DateUtil.format(date,"yyyy-MM-dd"),"yyyy-MM-dd");
			if(i == limitDayCount && !checkOpen(openTime)){
				break;
			}
			if(HolidayUtil.isHoliday(date)){
				//如果预约提前天数不用忽略假日 则累计
				if(!ignoreHoliday){
					i++;
				}
				continue;
			}
			List<RoomScheduleDto> list = roomService.getRoomScheduleArraign(DateUtil.format(date, "yyyy-MM-dd") ,null , userId);
			boolean available = false;
			if(list != null && list.size() > 0 ){
				for(RoomScheduleDto bean:list){
					if(bean.isRoomAvailable()){
						available = true;
						break;
					}
				}
			}
			NameCountResult c = new NameCountResult();
			c.setName(DateUtil.format(date,"yyyy-MM-dd"));
			if(available){
				c.setInfo("可预约");
				c.setStatus(true);
			}else{
				c.setInfo("已约满");
				c.setStatus(false);
			}
			result.add(c);
			i++;
		}
		return result;
	}

	/**
	 * 校验放号时间点
	 * @param openTimeStr
	 * @return
	 */
	public boolean checkOpen(String openTimeStr){
		Date openTime = DateUtil.parse(DateUtil.format(new Date(),"yyyy-MM-dd ")+ openTimeStr , "yyyy-MM-dd HH:mm");
		return new Date().getTime() > openTime.getTime();
	}

	/**
	 * 判断是否周末
	 * @param date
	 * @return
	 */
	public boolean isWeekend(Date date){
		int weekendDay = DateUtil.dayOfWeek(date);
		//周六是一周的第七天  周日是一周的第一天
		return weekendDay == 7 || weekendDay == 1;
	}

	public static void main(String[] args) {
		Date d = new Date();
		System.out.println(DateUtil.offsetDay(d,1));
	}

	/**
	 * @Author wangyong
	 * @Description 按照开始结束时间将数据进行分组
	 * @Date 15:34 2020-10-23
	 * @Param [param]
	 * @return java.util.Map<java.lang.String,java.util.List<com.mkst.umap.app.admin.api.bean.result.arraign.TimeApplyResult>>
	 */
	@Override
	public Map<String, List<TimeApplyResult>> selectTimeApplyList(ArraignAppointmentParam param) {

		// 一个Key降序排序的treeMap
		Map<String, List<TimeApplyResult>> result = new TreeMap<>(Comparator.reverseOrder());
		ArrayList<TimeApplyResult> container = new ArrayList<>();
		HashMap<String, String> colorContainer = new HashMap<>();

		// 查出各种时间状态的申请

		param.setNowStatus(KeyConstant.EVENT_PROGRESS_STATUS_FINISHED);
		container.addAll(arraignAppointmentMapper.selectTimeApplyList(param));
		param.setNowStatus(KeyConstant.EVENT_PROGRESS_STATUS_ONGOING);
		container.addAll(arraignAppointmentMapper.selectTimeApplyList(param));
		param.setNowStatus(KeyConstant.EVENT_PROGRESS_STATUS_WAITING);
		container.addAll(arraignAppointmentMapper.selectTimeApplyList(param));

		// 写入数据
		container.stream().forEach(timeApplyResult -> {
			if (!result.containsKey(timeApplyResult.getTimeCon())){
				List<TimeApplyResult> arrForTimeCon = new ArrayList<>();
				result.put(timeApplyResult.getTimeCon(),arrForTimeCon);
			}

			// 如果房间无初始数据，就初始一下
			if (!colorContainer.containsKey(timeApplyResult.getRoomName())){
				String color = new String();
				// 挑一个未被使用的颜色
				for (RoomColorEnum roomColorEnum : RoomColorEnum.values()) {
					if (colorContainer.containsValue(roomColorEnum.getValue())){
						continue;
					}
					color = roomColorEnum.getValue();
					break;
				}
				colorContainer.put(timeApplyResult.getRoomName(),color);
			}
			timeApplyResult.setRoomBackColor(colorContainer.get(timeApplyResult.getRoomName()));
			result.get(timeApplyResult.getTimeCon()).add(timeApplyResult);
		});

		return result;
	}

	@Override
	public Map<String,List<TimeApplyResult>> selectDayApplyList(ArraignAppointmentParam param){
		Date checkDate = param.getCheckDate();
		param = new ArraignAppointmentParam();
		param.setCheckDate(checkDate);
		param.setStatus(KeyConstant.EVENT_AUDIT_STATUS_PASS);
		ArrayList<TimeApplyResult> container = new ArrayList<>();
		param.setNowStatus(KeyConstant.EVENT_PROGRESS_STATUS_FINISHED);
		container.addAll(arraignAppointmentMapper.selectTimeApplyList(param));
		param.setNowStatus(KeyConstant.EVENT_PROGRESS_STATUS_ONGOING);
		container.addAll(arraignAppointmentMapper.selectTimeApplyList(param));
		param.setNowStatus(KeyConstant.EVENT_PROGRESS_STATUS_WAITING);
		container.addAll(arraignAppointmentMapper.selectTimeApplyList(param));

		log.info("------------------:{}",container);
		Map<String, TimeApplyResult> tempMap = null;
		if(container.size() > 0){
			// 场次排序
			tempMap = replaceSort(container);
		}
		Map<String,List<TimeApplyResult>> map = new HashMap<>();
		List<TimeApplyResult> morningList = new ArrayList<>();
		List<TimeApplyResult> afternoonList = new ArrayList<>();
		for(TimeApplyResult bean:container){
			if("1".equals(bean.getTimePie())){
				morningList.add(bean);
			}else{
				afternoonList.add(bean);
			}
		}
		map.put("morning",sortList(morningList,"1", tempMap));
		map.put("afternoon",sortList(afternoonList,"2", tempMap));
		log.info("------------------:{}", JSONObject.toJSONString(map));
		return map;
	}

	// 对换班的重新制定规则
	private Map<String, TimeApplyResult> replaceSort(List<TimeApplyResult> container){
		ArraignShift shift = new ArraignShift();
		shift.setApplySourceDate(container.get(0).getStartTime().substring(0, 10));
		// 换班的场次
		List<ArraignShift> shifts = arraignAppointmentMapper.selectDayShift(shift);

		// 找出换班的
		Map<String, TimeApplyResult> map = new HashMap<>();
		for(ArraignShift e : shifts){

			for(TimeApplyResult e2 : container){

				if(e.getApplyArraignAppointmentId().equals(e2.getId())){

					e2.setNumOrder(e.getTargetNumOrder());
					e2.setTimePie(e.getTargetTimePie());
					map.put(e2.getId().toString(), e2);
				}
				if(e.getTargetArraignAppointmentId().equals(e2.getId())){

					e2.setNumOrder(e.getApplyNumOrder());
					e2.setTimePie(e.getApplyTimePie());
					map.put(e2.getId().toString(), e2);
				}
			}

		}

		return map;
	}

	private List<TimeApplyResult> sortList(List<TimeApplyResult> list , String timePie, Map<String, TimeApplyResult> map){
		if(list == null || list.size() == 0){
			return null;
		}
//		Collections.sort(list, new Comparator<TimeApplyResult>() {
//			@Override
//			public int compare(TimeApplyResult o1, TimeApplyResult o2) {
//				if(o1.getNumOrder() != null && o2.getNumOrder() != null){
//					return o1.getNumOrder() - o2.getNumOrder();
//				}
//				return 0;
//			}
//		});
		List<TimeApplyResult> temp = new ArrayList<>();
		list = list.stream().filter(n -> {
			if("plus".equals(n.getRemark())){
				temp.add(n);
				return false;
			}
			return true;
		}).collect(Collectors.toList());
		list.addAll(temp);

		// 换班的固定排序
		if(map != null) {
			Set<String> keys = map.keySet();
			List<TimeApplyResult> sort = new ArrayList<>();

			for(String key : keys){
				sort.add(map.get(key));
			}
			sort = sort.stream().sorted(Comparator.comparing(TimeApplyResult::getNumOrder)).collect(Collectors.toList());
			TimeApplyResult e;
			int n;
			for(TimeApplyResult key : sort){
				for(int i = 0; i < list.size(); i++){
					e = list.get(i);
					if(key.getId().toString().equals(e.getId().toString())){
						n = e.getNumOrder() - 1;
						if(i <= n)
							list.add(Math.min(n+1, list.size()-1), e);
						else
							list.add(n, e);

						if(i <= n)
							list.remove(i);
						else
							list.remove(i+1);

						break;
					}
				}
			}
		}

		int i = 1;
		for(TimeApplyResult r:list){
			//if(r.getNumOrder() == null ||  r.getNumOrder().intValue()!=i){
			arraignAppointmentMapper.updateNumOrder(r.getId(),i , timePie);
			//}
			r.setNumOrder(i);
			i++;
		}

		return list;
	}

	@Override
	public void resetApplyOrder(long id , Integer numOrder ,  Date checkDate , String timePie){
		ArraignAppointment dbArraignAppointment = arraignAppointmentMapper.selectArraignAppointmentById(id);
		if(!KeyConstant.EVENT_AUDIT_STATUS_PASS.equals(dbArraignAppointment.getStatus())){
			//费
			return ;
		}
		ArraignAppointmentParam param = new ArraignAppointmentParam();
		param.setCheckDate(checkDate);
		param.setStatus(KeyConstant.EVENT_AUDIT_STATUS_PASS);
		ArrayList<TimeApplyResult> container = new ArrayList<>();
		param.setNowStatus(KeyConstant.EVENT_PROGRESS_STATUS_FINISHED);
		container.addAll(arraignAppointmentMapper.selectTimeApplyList(param));
		param.setNowStatus(KeyConstant.EVENT_PROGRESS_STATUS_ONGOING);
		container.addAll(arraignAppointmentMapper.selectTimeApplyList(param));
		param.setNowStatus(KeyConstant.EVENT_PROGRESS_STATUS_WAITING);
		container.addAll(arraignAppointmentMapper.selectTimeApplyList(param));
		Collections.sort(container, new Comparator<TimeApplyResult>() {
			@Override
			public int compare(TimeApplyResult o1, TimeApplyResult o2) {
				if(o1.getNumOrder() != null && o2.getNumOrder() != null){
					return o1.getNumOrder() - o2.getNumOrder();
				}
				return 0;
			}
		});
		arraignAppointmentMapper.updateNumOrder(id,numOrder,timePie);
		for(TimeApplyResult r:container){
			if(r.getId().equals(id)){
				continue;
			}
			//如果在同一半区
			if(timePie.equals(dbArraignAppointment.getTimePie())){
				if(timePie.equals(r.getTimePie())){
					if(dbArraignAppointment.getNumOrder() != null && dbArraignAppointment.getNumOrder() < numOrder){
						//调后 则需要把相应区间的场次提前
						if(r.getNumOrder() != null
								&&  r.getNumOrder()<=numOrder
								&&  r.getNumOrder()>dbArraignAppointment.getNumOrder()){
							arraignAppointmentMapper.updateNumOrder(r.getId(),r.getNumOrder()-1 , r.getTimePie());
						}
					}else{
						//调前  需要把相应场次推迟
						if(r.getNumOrder() != null && r.getNumOrder()>=numOrder ){
							arraignAppointmentMapper.updateNumOrder(r.getId(),r.getNumOrder()+1 , r.getTimePie());
						}
					}
				}
			}else{
				if(dbArraignAppointment.getTimePie().equals(r.getTimePie())){
					//
					if(r.getNumOrder() != null
							&&  r.getNumOrder()>=numOrder
					){
						arraignAppointmentMapper.updateNumOrder(r.getId(),r.getNumOrder()-1 , r.getTimePie());
					}
				}else{
					//不在同一半区 现在半区推后
					if(r.getNumOrder() != null
							&&  r.getNumOrder()>=numOrder
					){
						arraignAppointmentMapper.updateNumOrder(r.getId(),r.getNumOrder()+1 , r.getTimePie());
					}
				}
			}
		}
	}

	@Override
	public String importLog() {
		// 缓存
		HashMap<String, Object> cache = new HashMap<>();
		String filDir = profileDir + KeyConstant.ARRAIGN_DATA_FILE_NAME;
		FileReader fileReader = new FileReader(filDir);
		String result = fileReader.readString();
		List<ArraignData> arraignData = JSONArray.parseArray(result, ArraignData.class);
		Long index = 0L;
		for (ArraignData d : arraignData){
			ArraignAppointment insertArraign = new ArraignAppointment();
			try {
				initData(insertArraign,d,cache);
				this.insertArraignAppointment(insertArraign);
			} catch (Exception e) {
				index++;
				e.printStackTrace();
			}
		}
		return "本次导入：" + arraignData.size() +"条数据，其中失败："+index +"条！！！";
	}

	public boolean haveSyncArraign(){
		// TODO
		return false;
	}

	public String syncArraign(Set<ArraignSyncData> arraignSyncDataList){
		HashMap<String, Object> cache = new HashMap<>();
		Long index = 0L;
		for (ArraignSyncData d : arraignSyncDataList){
			try {
				ArraignAppointment insertArraign  = initArraignData(d,cache);
				if(insertArraign == null){
					continue;
				}
				//校验是否存在，如果存在状态不同则覆盖状态，不存在则插入
				ArraignAppointmentOld old = arraignAppointmentOldMapper.selectArraignAppointmentOldById(d.getId());
				if(old == null){
					//insert
					//判断新系统是否具有这条数据，无则插入，有则比对状态，状态不同则更新，一致则忽略
					//查系统
					ArraignAppointmentTotalDto dto = new ArraignAppointmentTotalDto();
					dto.setCriminalName(insertArraign.getCriminalName());
					//dto.setProsecutorName(insertArraign.getProsecutorName());
					List<ArraignAppointment> list = arraignAppointmentMapper.selectArraignAppointmentListByDto(dto);
					//校验库里是否存在
					ArraignAppointment exist = null;
					if(list != null && list.size() > 0 ){
						for(ArraignAppointment aam:list){
							if(DateUtil.isSameDay(aam.getStartTime() , insertArraign.getStartTime())){
								exist = aam;
							}
						}
					}
					old = new ArraignAppointmentOld();
					//如果数据已存在校验状态 状态太一样则不村里状态不一致则更新
					if(exist != null){
						if(!exist.getStatus().equals(insertArraign.getStatus()) && "0".equals(exist.getStatus())){

							exist.setStatus(insertArraign.getStatus());
							exist.setUpdateTime(DateUtil.date());
							arraignAppointmentMapper.updateArraignAppointment(exist);
						}
						old.setArraignAppointmentId(exist.getId());
					}else{
						this.insertArraignAppointment(insertArraign);
						old.setArraignAppointmentId(insertArraign.getId());
					}
					Date now = new Date();
					//插入临时表
					old.setOldId(d.getId());
					old.setPullTime(now);
					old.setUpdateTime(now);
					old.setStatus(insertArraign.getStatus());
					//插入
					arraignAppointmentOldMapper.insert(old);
				}else if(!old.getStatus().equals(insertArraign.getStatus())){
					//update
					arraignAppointmentMapper.updateArraignAppointment(insertArraign);
					old.setStatus(insertArraign.getStatus());
					arraignAppointmentOldMapper.updateStatus(old);
				}
			} catch (Exception e) {
				index++;
				e.printStackTrace();
			}
		}
		return "本次拉取：" + arraignSyncDataList.size() +"条数据，其中失败："+index +"条！！！";
	}

	private ArraignAppointment initArraignData(ArraignSyncData d , HashMap<String, Object> cache) throws Exception{
		ArraignAppointment insertArraign = new ArraignAppointment();
		// 申请人数据
		String undertaker = d.getUndertaker();
		String applyerLoginNameKey = "applyer" + undertaker;
		Object applyerLoginName = cache.get(applyerLoginNameKey);
		if (BeanUtil.isEmpty(applyerLoginName)){
			SysUser user = userService.selectUserByUserName(undertaker);
			if (BeanUtil.isNotEmpty(user)){
				cache.put(applyerLoginNameKey,user.getLoginName());
				insertArraign.setCreateBy(user.getLoginName());
			}
		}else {
			insertArraign.setCreateBy(applyerLoginName.toString());
		}
		//没有检查管则忽略
		if(insertArraign.getCreateBy() == null){
			return null;
		}
		// 申请时间数据
		Long applytime = d.getApplytime();
		if (BeanUtil.isNotEmpty(applytime)){
			insertArraign.setCreateTime(new Date(applytime));
		}

		// 罪犯生日数据
		String birthday = d.getBirthday();
		if (StringUtil.isNotEmpty(birthday)){
			insertArraign.setCriminalBirth(DateUtils.dateTime(DateUtils.YYYY_MM_DD,birthday));
		}

		// 检察官证件号数据
		insertArraign.setProsecutorId(d.getCertificateId());

		// 犯罪类型
		String crimetype = d.getCrimetype();
		String cType = "0";
		if (crimetype.contains("诈骗")){
			cType = "1";
		}else if(crimetype.contains("非法经营")){
			cType = "17";
		}else if(crimetype.contains("受贿")){
			cType = "18";
		}else if(crimetype.contains("国家机关文件")){
			cType = "19";
		}else if(crimetype.contains("重伤") || crimetype.contains("死亡")){
			cType = "2";
		}else if(crimetype.contains("强奸")){
			cType = "3";
		}else if(crimetype.contains("抢劫")){
			cType = "4";
		}else if(crimetype.contains("毒品")){
			cType = "5";
		}else {
			cType = "0";
		}
		insertArraign.setCriminalType(cType);

		// 罪犯姓名
		insertArraign.setCriminalName(d.getCriminal());

		// remark
		insertArraign.setRemark(d.getDescr());

		//检察官姓名&ID
		String prosecuteName = d.getProsecuteName();
		String prosecuteNameKey = "prosecuteName" + prosecuteName;
		Object prosecuteInfo = cache.get(prosecuteNameKey);
		if (BeanUtil.isEmpty(prosecuteInfo)){
			SysUser selectUser = new SysUser();
			selectUser.setUserName(prosecuteName);
			userService.selectUserByUserName(prosecuteName);
			SysUser user = userService.selectUserByUserName(prosecuteName);
			if (BeanUtil.isNotEmpty(user)){
				cache.put(prosecuteNameKey,user);
				insertArraign.setProsecutorUserId(user.getUserId());
				insertArraign.setProsecutorName(user.getUserName());
			}
		}else {
			insertArraign.setProsecutorUserId(((SysUser) prosecuteInfo).getUserId());
			insertArraign.setProsecutorName(((SysUser) prosecuteInfo).getUserName());
		}

		// 房间号
		// 这里不是很合适，应当模糊查询搜索数据库表
		// 但是无法保证数据正确性，无论如何，结果总是错误的
		if (d.getRoomtype() == 1){
			insertArraign.setRoomId("ad4e7a66b11411ea8ab91c1b0dbdb961");
		}else {
			insertArraign.setRoomId("1a0f004ab13511ea8ab91c1b0dbdb961");
		}

		// 状态
		insertArraign.setStatus(String.valueOf(d.getStatus()));
		// 开始时间
		Long time1 = d.getTime1();
		if (BeanUtil.isNotEmpty(time1)){
			insertArraign.setStartTime(new Date(time1));
		}
		// 结束时间
		Long time2 = d.getTime2();
		if (BeanUtil.isNotEmpty(time2)){
			insertArraign.setEndTime(new Date(time2));
		}
		if(d.getDescr() != null && d.getDescr().contains("捕")){
			insertArraign.setStage("0");
		}else{
			insertArraign.setStage("1");
		}
		// 认罪认罚
		if(d.getDescr() != null && d.getDescr().contains("认罪认罚")){
			insertArraign.setCriminalAdmit("Y");
		}else{
			insertArraign.setCriminalAdmit("N");
		}
		// 初始化一些数据
		insertArraign.setNeedLegalAid("N");
		return insertArraign;
	}

	private void initData(ArraignAppointment insertArraign, ArraignData d, HashMap<String, Object> cache) throws Exception {

		// 申请人数据
		String undertaker = d.getUndertaker();
		String applyerLoginNameKey = "applyer" + undertaker;
		Object applyerLoginName = cache.get(applyerLoginNameKey);
		if (BeanUtil.isEmpty(applyerLoginName)){
			SysUser user = userService.selectUserByUserName(undertaker);
			if (BeanUtil.isNotEmpty(user)){
				cache.put(applyerLoginNameKey,user.getLoginName());
				insertArraign.setCreateBy(user.getLoginName());
			}
		}else {
			insertArraign.setCreateBy(applyerLoginName.toString());
		}

		// 申请时间数据
		Long applytime = d.getApplytime();
		if (BeanUtil.isNotEmpty(applytime)){
			insertArraign.setCreateTime(new Date(applytime));
		}

		// 罪犯生日数据
		String birthday = d.getBirthday();
		if (StringUtil.isNotEmpty(birthday)){
			insertArraign.setCriminalBirth(DateUtils.dateTime(DateUtils.YYYY_MM_DD,birthday));
		}

		// 检察官证件号数据
		insertArraign.setProsecutorId(d.getCertificateId());

		// 犯罪类型
		String crimetype = d.getCrimetype();
		String cType = "0";
		if (crimetype.contains("诈骗")){
			cType = "1";
		}else if(crimetype.contains("非法经营")){
			cType = "17";
		}else if(crimetype.contains("受贿")){
			cType = "18";
		}else if(crimetype.contains("国家机关文件")){
			cType = "19";
		}else if(crimetype.contains("重伤") || crimetype.contains("死亡")){
			cType = "2";
		}else if(crimetype.contains("强奸")){
			cType = "3";
		}else if(crimetype.contains("抢劫")){
			cType = "4";
		}else if(crimetype.contains("毒品")){
			cType = "5";
		}else {
			cType = "0";
		}
		insertArraign.setCriminalType(cType);

		// 罪犯姓名
		insertArraign.setCriminalName(d.getCriminal());

		// remark
		insertArraign.setRemark(d.getDescr());

		//检察官姓名&ID
		String prosecuteName = d.getProsecuteName();
		String prosecuteNameKey = "prosecuteName" + prosecuteName;
		Object prosecuteInfo = cache.get(prosecuteNameKey);
		if (BeanUtil.isEmpty(prosecuteInfo)){
			SysUser selectUser = new SysUser();
			selectUser.setUserName(prosecuteName);
			userService.selectUserByUserName(prosecuteName);
			SysUser user = userService.selectUserByUserName(prosecuteName);
			if (BeanUtil.isNotEmpty(user)){
				cache.put(prosecuteNameKey,user);
				insertArraign.setProsecutorUserId(user.getUserId());
				insertArraign.setProsecutorName(user.getUserName());
			}
		}else {
			insertArraign.setProsecutorUserId(((SysUser) prosecuteInfo).getUserId());
			insertArraign.setProsecutorName(((SysUser) prosecuteInfo).getUserName());
		}

		// 房间号
		// 这里不是很合适，应当模糊查询搜索数据库表
		// 但是无法保证数据正确性，无论如何，结果总是错误的
		if (d.getRoomtype() == 1){
			insertArraign.setRoomId("ad4e7a66b11411ea8ab91c1b0dbdb961");
		}else {
			insertArraign.setRoomId("1a0f004ab13511ea8ab91c1b0dbdb961");
		}

		// 状态
		insertArraign.setStatus(String.valueOf(d.getStatus()));
		// 开始时间
		Long time1 = d.getTime1();
		if (BeanUtil.isNotEmpty(time1)){
			insertArraign.setStartTime(new Date(time1));
		}
		// 结束时间
		Long time2 = d.getTime2();
		if (BeanUtil.isNotEmpty(time2)){
			insertArraign.setEndTime(new Date(time2));
		}

		// 初始化一些数据
		insertArraign.setStage("1");
		insertArraign.setCriminalAdmit("N");
		insertArraign.setNeedLegalAid("N");


	}

	/**
	 * 结束提审
	 * @param id 提审预约ID
	 * @param finishBy 操作人
	 */
	public int finishArraignAppoint(Long id , String finishBy){
		return arraignAppointmentMapper.finish(id , "1" , finishBy , new Date());
	}

	public List<ArraignAppointment> selectNotFinishArraignAppointment(Long prosecutorUserId){
		return arraignAppointmentMapper.selectNotFinishAppointment(prosecutorUserId
				, DateUtil.format(new Date(),"yyyy-MM-dd") , "0" , KeyConstant.EVENT_AUDIT_STATUS_PASS);
	}

	public List<SysUser> selectProsecutorList(SelectProcustorParam param){
		return arraignAppointmentMapper.selectProsecutorList(param);
	}

	@Override
	public int modifyFinishStatus() {
		return arraignAppointmentMapper.modifyFinishStatus();
	}

	@Override
	public int isArraign(ArraignAppointment appointment) {
		return arraignAppointmentMapper.isArraign(appointment);
	}
}
